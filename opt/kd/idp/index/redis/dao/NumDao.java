package kd.idp.index.redis.dao;

import java.util.Map;

import kd.idp.index.redis.bean.Num;

public interface NumDao {
    public void save(Num num);
    public Map<String,Object> find();
    public void saveLine(Num num);
	public Map<String, Object> findLine();
}
