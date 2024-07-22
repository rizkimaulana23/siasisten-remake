package siasisten_remake.demo.event.application_listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import siasisten_remake.demo.event.application_event.DiterimaLowonganEvent;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;

@Component
@Slf4j
public class DiterimaLowonganListener {

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @EventListener
    public void handleLowonganDiterima(DiterimaLowonganEvent event) {
        lowonganMataKuliahRepository.updateJumlahDiterimaSetelahPendaftaran(event.getKode_lmk());
    }
}
