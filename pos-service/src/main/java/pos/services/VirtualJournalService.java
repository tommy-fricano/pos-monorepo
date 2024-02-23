package pos.services;

import pos.controllers.Register;
import pos.listeners.RegisterEvent;
import pos.listeners.RegisterEventListener;
import pos.server.Server;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VirtualJournalService implements RegisterEventListener {

    public static final String VIRTUAL_JOURNAL_LOG = "Virtual Journal: ";
    private final Server server;
    private final Register register;

    @PostConstruct
    private void begin(){
        register.addRegisterEventListener(this);
    }


    @Override
    public void updateListeners(RegisterEvent event) {
        switch (event.getAction()) {
            case STARTBASKET -> basketInitializedLog(event);
            case ADDITEM -> itemLog(event);
            case VOIDITEM -> itemLog(event);
            default -> basketCompleteLog(event);
        }
    }

    public void itemLog(RegisterEvent registerEvent) {
        server.broadcast(VIRTUAL_JOURNAL_LOG + registerEvent.buildLineItemString(registerEvent.getLastItem()));
        System.out.println(VIRTUAL_JOURNAL_LOG  + registerEvent.buildLineItemString(registerEvent.getLastItem()));
    }

    public void basketInitializedLog(RegisterEvent event){
        server.broadcast(VIRTUAL_JOURNAL_LOG + event.getAction() );
        System.out.println(VIRTUAL_JOURNAL_LOG + event.getAction());
    }

    public void basketCompleteLog(RegisterEvent event){
        StringBuilder log = new StringBuilder();
        log.append(VIRTUAL_JOURNAL_LOG).append(event.buildEventString());
        server.broadcast(String.valueOf(log));
        System.out.println(log);
    }

}
