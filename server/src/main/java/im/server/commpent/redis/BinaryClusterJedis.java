package im.server.commpent.redis;

import redis.clients.jedis.*;
import redis.clients.jedis.commands.BinaryJedisCommands;
import redis.clients.jedis.params.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cch
 * @date 2021/10/28 14:51
 * cluster模式操作。（真棒，代码又臭又长）
 * TODO 用到对应的方法再去改一下，懒得调整了
 */
public class BinaryClusterJedis implements BinaryJedisCommands {

    private JedisCluster cluster;

    public BinaryClusterJedis(JedisCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return cluster.set(key, value);
    }

    @Override
    public String set(byte[] key, byte[] value, SetParams params) {
        return cluster.set(key, value, params);
    }

    @Override
    public byte[] get(byte[] key) {
        return cluster.get(key);
    }

    @Override
    public byte[] getDel(byte[] key) {
        return cluster.getDel(key);
    }

    @Override
    public byte[] getEx(byte[] key, GetExParams params) {
        return new byte[0];
    }

    @Override
    public Boolean exists(byte[] key) {
        return null;
    }

    @Override
    public Long persist(byte[] key) {
        return null;
    }

    @Override
    public String type(byte[] key) {
        return null;
    }

    @Override
    public byte[] dump(byte[] key) {
        return new byte[0];
    }

    @Override
    public String restore(byte[] key, long ttl, byte[] serializedValue) {
        return null;
    }

    @Override
    public String restoreReplace(byte[] key, long ttl, byte[] serializedValue) {
        return null;
    }

    @Override
    public String restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
        return null;
    }

    @Override
    public Long expire(byte[] key, long seconds) {
        return null;
    }

    @Override
    public Long pexpire(byte[] key, long milliseconds) {
        return null;
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        return null;
    }

    @Override
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return null;
    }

    @Override
    public Long ttl(byte[] key) {
        return null;
    }

    @Override
    public Long pttl(byte[] key) {
        return null;
    }

    @Override
    public Long touch(byte[] key) {
        return null;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, boolean value) {
        return null;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return null;
    }

    @Override
    public Boolean getbit(byte[] key, long offset) {
        return null;
    }

    @Override
    public Long setrange(byte[] key, long offset, byte[] value) {
        return null;
    }

    @Override
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return new byte[0];
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return new byte[0];
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public String setex(byte[] key, long seconds, byte[] value) {
        return null;
    }

    @Override
    public String psetex(byte[] key, long milliseconds, byte[] value) {
        return null;
    }

    @Override
    public Long decrBy(byte[] key, long decrement) {
        return null;
    }

    @Override
    public Long decr(byte[] key) {
        return null;
    }

    @Override
    public Long incrBy(byte[] key, long increment) {
        return null;
    }

    @Override
    public Double incrByFloat(byte[] key, double increment) {
        return null;
    }

    @Override
    public Long incr(byte[] key) {
        return null;
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public byte[] substr(byte[] key, int start, int end) {
        return new byte[0];
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return null;
    }

    @Override
    public Long hset(byte[] key, Map<byte[], byte[]> hash) {
        return null;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return new byte[0];
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return null;
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return null;
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return null;
    }

    @Override
    public Long hincrBy(byte[] key, byte[] field, long value) {
        return null;
    }

    @Override
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return null;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        return null;
    }

    @Override
    public Long hdel(byte[] key, byte[]... field) {
        return null;
    }

    @Override
    public Long hlen(byte[] key) {
        return null;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> hvals(byte[] key) {
        return null;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return null;
    }

    @Override
    public byte[] hrandfield(byte[] key) {
        return new byte[0];
    }

    @Override
    public List<byte[]> hrandfield(byte[] key, long count) {
        return null;
    }

    @Override
    public Map<byte[], byte[]> hrandfieldWithValues(byte[] key, long count) {
        return null;
    }

    @Override
    public Long rpush(byte[] key, byte[]... args) {
        return cluster.rpush(key, args);
    }

    @Override
    public Long lpush(byte[] key, byte[]... args) {
        return null;
    }

    @Override
    public Long llen(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long stop) {
        return cluster.lrange(key, start, stop);
    }

    @Override
    public String ltrim(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public byte[] lindex(byte[] key, long index) {
        return new byte[0];
    }

    @Override
    public String lset(byte[] key, long index, byte[] value) {
        return null;
    }

    @Override
    public Long lrem(byte[] key, long count, byte[] value) {
        return null;
    }

    @Override
    public byte[] lpop(byte[] key) {
        return new byte[0];
    }

    @Override
    public List<byte[]> lpop(byte[] key, int count) {
        return null;
    }

    @Override
    public Long lpos(byte[] key, byte[] element) {
        return null;
    }

    @Override
    public Long lpos(byte[] key, byte[] element, LPosParams params) {
        return null;
    }

    @Override
    public List<Long> lpos(byte[] key, byte[] element, LPosParams params, long count) {
        return null;
    }

    @Override
    public byte[] rpop(byte[] key) {
        return new byte[0];
    }

    @Override
    public List<byte[]> rpop(byte[] key, int count) {
        return null;
    }

    @Override
    public Long sadd(byte[] key, byte[]... member) {
        return null;
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        return null;
    }

    @Override
    public Long srem(byte[] key, byte[]... member) {
        return null;
    }

    @Override
    public byte[] spop(byte[] key) {
        return new byte[0];
    }

    @Override
    public Set<byte[]> spop(byte[] key, long count) {
        return null;
    }

    @Override
    public Long scard(byte[] key) {
        return null;
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public List<Boolean> smismember(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public byte[] srandmember(byte[] key) {
        return new byte[0];
    }

    @Override
    public List<byte[]> srandmember(byte[] key, int count) {
        return null;
    }

    @Override
    public Long strlen(byte[] key) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return null;
    }

    @Override
    public Double zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
        return null;
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public Long zrem(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public Double zincrby(byte[] key, double increment, byte[] member) {
        return null;
    }

    @Override
    public Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
        return null;
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrange(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public byte[] zrandmember(byte[] key) {
        return new byte[0];
    }

    @Override
    public Set<byte[]> zrandmember(byte[] key, long count) {
        return null;
    }

    @Override
    public Set<Tuple> zrandmemberWithScores(byte[] key, long count) {
        return null;
    }

    @Override
    public Long zcard(byte[] key) {
        return null;
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public List<Double> zmscore(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public Tuple zpopmax(byte[] key) {
        return null;
    }

    @Override
    public Set<Tuple> zpopmax(byte[] key, int count) {
        return null;
    }

    @Override
    public Tuple zpopmin(byte[] key) {
        return null;
    }

    @Override
    public Set<Tuple> zpopmin(byte[] key, int count) {
        return null;
    }

    @Override
    public List<byte[]> sort(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return null;
    }

    @Override
    public Long zcount(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByRank(byte[] key, long start, long stop) {
        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Long linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
        return null;
    }

    @Override
    public Long lpushx(byte[] key, byte[]... arg) {
        return null;
    }

    @Override
    public Long rpushx(byte[] key, byte[]... arg) {
        return null;
    }

    @Override
    public Long del(byte[] key) {
        return null;
    }

    @Override
    public Long unlink(byte[] key) {
        return null;
    }

    @Override
    public byte[] echo(byte[] arg) {
        return new byte[0];
    }

    @Override
    public Long move(byte[] key, int dbIndex) {
        return null;
    }

    @Override
    public Long bitcount(byte[] key) {
        return null;
    }

    @Override
    public Long bitcount(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Long pfadd(byte[] key, byte[]... elements) {
        return null;
    }

    @Override
    public long pfcount(byte[] key) {
        return 0;
    }

    @Override
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return null;
    }

    @Override
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return null;
    }

    @Override
    public Long geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return null;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return null;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return null;
    }

    @Override
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public List<Long> bitfield(byte[] key, byte[]... arguments) {
        return null;
    }

    @Override
    public List<Long> bitfieldReadonly(byte[] key, byte[]... arguments) {
        return null;
    }

    @Override
    public Long hstrlen(byte[] key, byte[] field) {
        return null;
    }

    @Override
    public byte[] xadd(byte[] key, byte[] id, Map<byte[], byte[]> hash, long maxLen, boolean approximateLength) {
        return new byte[0];
    }

    @Override
    public byte[] xadd(byte[] key, Map<byte[], byte[]> hash, XAddParams params) {
        return new byte[0];
    }

    @Override
    public Long xlen(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> xrange(byte[] key, byte[] start, byte[] end) {
        return null;
    }

    @Override
    public List<byte[]> xrange(byte[] key, byte[] start, byte[] end, int count) {
        return null;
    }

    @Override
    public List<byte[]> xrevrange(byte[] key, byte[] end, byte[] start) {
        return null;
    }

    @Override
    public List<byte[]> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
        return null;
    }

    @Override
    public Long xack(byte[] key, byte[] group, byte[]... ids) {
        return null;
    }

    @Override
    public String xgroupCreate(byte[] key, byte[] consumer, byte[] id, boolean makeStream) {
        return null;
    }

    @Override
    public String xgroupSetID(byte[] key, byte[] consumer, byte[] id) {
        return null;
    }

    @Override
    public Long xgroupDestroy(byte[] key, byte[] consumer) {
        return null;
    }

    @Override
    public Long xgroupDelConsumer(byte[] key, byte[] consumer, byte[] consumerName) {
        return null;
    }

    @Override
    public Long xdel(byte[] key, byte[]... ids) {
        return null;
    }

    @Override
    public Long xtrim(byte[] key, long maxLen, boolean approximateLength) {
        return null;
    }

    @Override
    public Long xtrim(byte[] key, XTrimParams params) {
        return null;
    }

    @Override
    public Object xpending(byte[] key, byte[] groupname) {
        return null;
    }

    @Override
    public List<Object> xpending(byte[] key, byte[] groupname, byte[] start, byte[] end, int count, byte[] consumername) {
        return null;
    }

    @Override
    public List<Object> xpending(byte[] key, byte[] groupname, XPendingParams params) {
        return null;
    }

    @Override
    public List<byte[]> xclaim(byte[] key, byte[] groupname, byte[] consumername, long minIdleTime, long newIdleTime, int retries, boolean force, byte[]... ids) {
        return null;
    }

    @Override
    public List<byte[]> xclaim(byte[] key, byte[] group, byte[] consumername, long minIdleTime, XClaimParams params, byte[]... ids) {
        return null;
    }

    @Override
    public List<byte[]> xclaimJustId(byte[] key, byte[] group, byte[] consumername, long minIdleTime, XClaimParams params, byte[]... ids) {
        return null;
    }

    @Override
    public StreamInfo xinfoStream(byte[] key) {
        return null;
    }

    @Override
    public Object xinfoStreamBinary(byte[] key) {
        return null;
    }

    @Override
    public List<StreamGroupInfo> xinfoGroup(byte[] key) {
        return null;
    }

    @Override
    public List<Object> xinfoGroupBinary(byte[] key) {
        return null;
    }

    @Override
    public List<StreamConsumersInfo> xinfoConsumers(byte[] key, byte[] group) {
        return null;
    }

    @Override
    public List<Object> xinfoConsumersBinary(byte[] key, byte[] group) {
        return null;
    }
}
