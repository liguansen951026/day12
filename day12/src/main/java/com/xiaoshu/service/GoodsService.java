package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.TypeMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.GoodsVo;
import com.xiaoshu.entity.Type;

import redis.clients.jedis.Jedis;

@Service
public class GoodsService {

	@Autowired
	private GoodsMapper gm;
	
	@Autowired
	private TypeMapper tm;

	public PageInfo<GoodsVo> findPage(GoodsVo goodsVo, Integer pageNum, Integer pageSize, String ordername, String order){
		PageHelper.startPage(pageNum, pageSize);
		List<GoodsVo> list = gm.findPage(goodsVo);
		return new PageInfo<GoodsVo>(list);
	}

	public List<Type> findTpye() {
		List<Type> list = tm.selectAll();
		return list;
	}

	public void deleteGoods(Integer id) {
		gm.deleteByPrimaryKey(id);
	}

	public Goods existGoodsWithGoodsName(String name) {
		Goods param = new Goods();
		param.setName(name);
		return gm.selectOne(param );
		
	}

	public void updateGoods(Goods goods) {
		gm.updateByPrimaryKeySelective(goods);
	}

	public void addGoods(Goods goods) {
		// TODO Auto-generated method stub
		gm.insert(goods);
	}
	public void addType(Type type) {
		// TODO Auto-generated method stub
		tm.insert(type);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		Type pram = new Type();
		pram.setTypename(type.getTypename());
		Type type2 = tm.selectOne(pram);
		jedis.hset("信息", type2.getId()+"", type2.getTypename());
	}
	
	
	public Type seleteName(String typename) {
		Type param = new Type();
		param.setTypename(typename);
		return tm.selectOne(param );
	}
	public List<GoodsVo> countGoods(){
		return gm.countGoods();
	}
	
	
}
