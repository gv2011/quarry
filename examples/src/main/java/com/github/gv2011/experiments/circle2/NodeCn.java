package com.github.gv2011.experiments.circle2;

import java.util.Optional;

public interface NodeCn extends Node{
	
	void enter(NodeCn newNode);
	
	void setLeftLeaving(NodeCn oldNode, Optional<NodeCn> newNode);

	void setRightLeaving(NodeCn oldNode, Optional<NodeCn> newNode);
	
	public void setRight(NodeCn newNode);

	public void setLeft(NodeCn newNode);

}
