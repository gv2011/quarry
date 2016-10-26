package com.github.gv2011.experiments.circle2;

public class Factory {

	private boolean useCache;

	public Factory(boolean useCache) {
		this.useCache = useCache;
	}
	
	NodeCtrl createNode(){
		return createNode(new Position());		
	}
	
	Cache createCache(NodeCn node){
		return useCache?new CacheImp(node): new EmptyCache();
		
	}

	public NodeCtrl createNode(Position position) {
		return new NodeImp(position, this);		
	}

}
