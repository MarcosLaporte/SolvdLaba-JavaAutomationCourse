package entities.lists;

import entities.Technician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "technicians")
public class TechnicianList {
    private List<Technician> technicians;

    @XmlElement(name = "technician")
    public List<Technician> getTechnicians() {
        return this.technicians;
    }

    public void setTechnicians(List<Technician> technicians) {
        this.technicians = technicians;
    }
}
