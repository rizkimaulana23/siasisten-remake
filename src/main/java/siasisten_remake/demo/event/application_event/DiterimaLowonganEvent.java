package siasisten_remake.demo.event.application_event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DiterimaLowonganEvent extends ApplicationEvent {

    private String kode_lmk;

    public DiterimaLowonganEvent(Object source, String kode_lmk) {
        super(source);
        this.kode_lmk = kode_lmk;
    }
}
