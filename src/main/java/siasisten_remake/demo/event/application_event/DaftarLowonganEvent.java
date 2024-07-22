package siasisten_remake.demo.event.application_event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DaftarLowonganEvent extends ApplicationEvent {

    private String kodeLmk;

    public DaftarLowonganEvent(Object source, String kodeLmk) {
        super(source);
        this.kodeLmk = kodeLmk;
    }
}
