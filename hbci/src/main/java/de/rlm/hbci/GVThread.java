package de.rlm.hbci;

import java.util.concurrent.Exchanger;

import de.rlm.hbci.common.Tools;


public class GVThread<T extends Object> {
	
	public GVResult<T> execute(UserRequest userRequest, GV<T> gv) throws HbciException{
		return runSynchronousThread(userRequest, gv);
	}
	
	private GVResult<T> runSynchronousThread(final UserRequest userRequest, final GV<T> gv) throws HbciException{
		
		GVResult<T> result = null;
		
		final Exchanger<GVResult<T>> container = new Exchanger<GVResult<T>>();
		
		ThreadGroup threadGroup = Tools.getOrCreateUserThreadGroup(userRequest);

		new Thread(threadGroup, "thread_" + userRequest.getBlz() + "_" + userRequest.getUserId()) {
			@Override
			public void run() {
				Session session = null;
				try{
//					session = SessionFactory.createSession(userRequest);
					session = SessionFactory.getOrCreateSession(userRequest);
					GVResult<T> result = gv.execute(session);
					container.exchange(result);
				} catch (RuntimeException e1){
					throw e1;
				} catch (Exception e){
					throw new RuntimeException(e);
				} finally {
					//TODO destroy has to be called with websession#destory or in servlet#request or smthg else and not with every access
//					if (session!=null){
//						session.destroy();
//					}
					interrupt();
				}
				
			}
		}.start();
		
		try{
			//TODO check for interruptions, exceptions etc.
			result = container.exchange(null);
		} catch (InterruptedException e){
			throw new HbciException(e);
		}
		return result;
	}

//	private GVResult<T> runSynchronousThread(final UserRequest userRequest, final GV<T> gv) throws HbciException{
//		
//		final Lock lock = new ReentrantLock();
//		final Condition condition = lock.newCondition();
//		
//		final List<GVResult<T>> container = new ArrayList<GVResult<T>>();
//		
//		ThreadGroup threadGroup = new ThreadGroup("threadGroup_" + userRequest.getBlz() + "_" + userRequest.getUserId());
//		lock.lock();
//		new Thread(threadGroup, "thread_" + userRequest.getBlz() + "_" + userRequest.getUserId()) {
//			@Override
//			public void run() {
//				Session session = null;
//				try{
//					session = SessionFactory.createSession(userRequest);
//					GVResult<T> result = gv.execute(session);
//					container.add(result);
//				} catch (Exception e){
//					throw new RuntimeException(e);
//				} finally {
//					if (session!=null){
//						session.destroy();
//					}
//					condition.signalAll();
//				}
//				
//			}
//		}.start();
//		
//		try{
//			condition.await();
//		} catch (InterruptedException e){
//			throw new HbciException(e);
//		} finally {
//			lock.unlock();
//		}
//		if (container.size()>0){
//			return container.get(0);
//		}
//		return null;
//	}
}
