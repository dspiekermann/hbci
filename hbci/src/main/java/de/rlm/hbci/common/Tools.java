package de.rlm.hbci.common;

import java.util.HashMap;
import java.util.Map;

import de.rlm.hbci.UserRequest;

public class Tools {
	
	private static final InheritableThreadLocal<Map<String, ThreadGroup>> THREADGROUP = new InheritableThreadLocal<Map<String,ThreadGroup>>();
	
	public static synchronized ThreadGroup getOrCreateUserThreadGroup(UserRequest userRequest){
		Map<String, ThreadGroup> map = THREADGROUP.get();
		if (map==null){
			map = new HashMap<String, ThreadGroup>();
			THREADGROUP.set(map);
		}
		ThreadGroup threadGroup = map.get(userRequest.getBlz() + "_" + userRequest.getUserId());
		if (threadGroup==null){
			threadGroup = new ThreadGroup("threadGroup_" + userRequest.getBlz() + "_" + userRequest.getUserId());
			map.put(userRequest.getBlz() + "_" + userRequest.getUserId(), threadGroup);
		}
		return threadGroup;
	}

	public static synchronized ThreadGroup getUserThreadGroup(UserRequest userRequest){
		Map<String, ThreadGroup> map = THREADGROUP.get();
		if (map!=null){
			return map.get(userRequest.getBlz() + "_" + userRequest.getUserId());
		}
		return null;
	}
}
