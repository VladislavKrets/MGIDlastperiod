package online.omnia.MGID;

/**
 * Created by lollipop on 28.11.2017.
 */
public class JsonSourceEntity {
    private int clicks;
    private double spent;
    private double cpc;
    private int buyCost;
    private double qualityFactor;

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getCpc() {
        return cpc;
    }

    public void setCpc(double cpc) {
        this.cpc = cpc;
    }

    public int getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(int buyCost) {
        this.buyCost = buyCost;
    }

    public double getQualityFactor() {
        return qualityFactor;
    }

    public void setQualityFactor(double qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

    @Override
    public String toString() {
        return "JsonSourceEntity{" +
                "clicks=" + clicks +
                ", spent=" + spent +
                ", cpc=" + cpc +
                ", buyCost=" + buyCost +
                ", qualityFactor=" + qualityFactor +
                '}';
    }
}
