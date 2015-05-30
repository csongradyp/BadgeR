package net.csongradyp.badger.parser.json.domain;

public class RangeTrigger<T> {

    private T start;
    private T end;

    public RangeTrigger() {
    }

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }
}
