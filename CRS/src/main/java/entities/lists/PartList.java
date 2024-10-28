package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Part;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "parts")
public class PartList implements IEntityList<Part> {
    @JsonProperty("parts")
    private List<Part> partList;

    private PartList() {
    }

    private PartList(List<Part> partList) {
        this.partList = partList;
    }

    @XmlElement(name = "part")
    @Override
    public List<Part> getList() {
        return this.partList;
    }

    @Override
    public void setList(List<Part> partList) {
        this.partList = partList;
    }
}
