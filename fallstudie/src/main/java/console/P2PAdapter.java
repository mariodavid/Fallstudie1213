package console;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import distribution.DistributionFactory;
import distribution.DistributionStrategy;

public class P2PAdapter {

	/**
	 * the strategy that is used by the p2p network to distribute the triples. 1
	 * = OneKeyDistribution (h(s),h(p),h(o)) 2 = TwoKeyDistribution
	 * (h(sp),h(po),h(so)) 3 = ThreeKeyDistribution (h(spo))
	 */
	private static final int	DEFAULT_DISTRIBUTION_STRATEGY	= 1;
	public Peer					peer;
	public DistributionStrategy	distributionStrategy;

	public P2PAdapter(Peer peer) {
		this.peer = peer;
		initDistributionStrategy();
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public DistributionStrategy getDistributionStrategy() {
		return distributionStrategy;
	}

	public void setDistributionStrategy(
			DistributionStrategy distributionStrategy) {
		this.distributionStrategy = distributionStrategy;
		this.distributionStrategy.setPeer(peer);
	}

	private void initDistributionStrategy() {
		distributionStrategy = DistributionFactory
				.create(DEFAULT_DISTRIBUTION_STRATEGY);
		distributionStrategy.setPeer(peer);
	}

	/**
	 * stellt die Verbindung zum P2P Netzwerk her
	 */
	public void connect() {

		peer = new Peer(Number160.createHash("sad"));
		try {
			peer.listen(4000, 4000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FutureBootstrap fb = peer.bootstrapBroadcast(4000);
		fb.awaitUninterruptibly();

	}

}
