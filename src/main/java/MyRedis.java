import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 *desc
 *
 *@author xym
 *@create 2017-06-07-10:13
 */
public class MyRedis {

	public static void main(String[] args) {
		testSentinel();
	}

	private static void testSentinel() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//TODO--
		String masterName = "mymaster"; //名称

		Set<String> sentinels = new HashSet<String>();

		sentinels.add("192.168.100.238:26379");
		sentinels.add("192.168.100.238:26380");

		//SentinelPool
		JedisSentinelPool jsp = new JedisSentinelPool(masterName, sentinels, poolConfig);

		int i = 0;
		while (true) {
			i++;
			//可以获取主节点信息
			HostAndPort currentHostMaster = jsp.getCurrentHostMaster();
			System.out.println(currentHostMaster + " -- " + i);
			Jedis resource = null;
			try {
				resource = jsp.getResource();
				System.out.println("resource" + resource);
				resource.set("xym_" + i, i + "val");
				System.out.println("get--" + i + "--" + resource.get("xym_" + i));
				new Thread().sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (resource != null) {
					resource.close();
				}
			}
		}
	}

	//private static void testServer() {
	//	Jedis jedis = new Jedis("192.168.100.238", 26379);
	//	List<String> masterAddr = jedis.sentinelGetMasterAddrByName("mymaster");
	//	for (String addr : masterAddr) {
	//		System.out.println(addr);
	//	}
	//}
}
