package entities.lists;

import entities.RepairTicket;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTickets")
public class RepairTicketList {
    private List<RepairTicket> repairTickets;

    @XmlElement(name = "repairTicket")
    public List<RepairTicket> getRepairTickets() {
        return this.repairTickets;
    }

    public void setRepairTickets(List<RepairTicket> repairTickets) {
        this.repairTickets = repairTickets;
    }
}
