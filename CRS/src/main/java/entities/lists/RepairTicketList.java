package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.RepairTicket;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTickets")
public class RepairTicketList implements IEntityList<RepairTicket> {
    @JsonProperty("repairTickets")
    private List<RepairTicket> repairTicketList;

    private RepairTicketList() {
    }

    private RepairTicketList(List<RepairTicket> repairTicketList) {
        this.repairTicketList = repairTicketList;
    }

    @XmlElement(name = "repairTicket")
    @Override
    public List<RepairTicket> getList() {
        return this.repairTicketList;
    }

    @Override
    public void setList(List<RepairTicket> repairTicketList) {
        this.repairTicketList = repairTicketList;
    }
}
