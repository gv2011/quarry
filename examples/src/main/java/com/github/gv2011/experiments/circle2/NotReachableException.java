package com.github.gv2011.experiments.circle2;

public class NotReachableException extends RuntimeException {

	private NodeCn node;

	public NotReachableException(NodeCn node, String msg) {
		super(msg);
		this.node = node;
	}

	public NotReachableException(NodeCn node) {
		this.node = node;
	}

	public NodeCn getNode() {
		return node;
	}
}
