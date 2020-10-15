package AIinterfaces.NetworkIF;

public interface CPPNNetworkIF extends HNNetworkIF{
    Integer getBestOrgID();

    CPPNNetworkIF getCompatibilityNetwork();

    void setCompatibilityNetwork();

    double[] runSubstrate(float[] agentVision);

    CPPNNetworkIF getCPPNetwork();

    CPPNNetworkIF clone();
}
