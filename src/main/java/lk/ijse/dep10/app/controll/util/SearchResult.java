package lk.ijse.dep10.app.controll.util;

public class SearchResult {
    public int getStart() {
        return start;
    }

    public SearchResult(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    private int start;
    private int end;

    @Override
    public String toString() {
        return "SearchResult{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
