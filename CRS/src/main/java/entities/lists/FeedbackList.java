package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Feedback;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "feedbacks")
public class FeedbackList implements IEntityList<Feedback> {
    @JsonProperty("feedbacks")
    private List<Feedback> feedbackList;

    private FeedbackList() {
    }

    private FeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @XmlElement(name = "feedback")
    public List<Feedback> getList() {
        return this.feedbackList;
    }

    @Override
    public void setList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }
}