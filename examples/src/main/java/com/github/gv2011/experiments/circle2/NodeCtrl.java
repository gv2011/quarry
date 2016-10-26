package com.github.gv2011.experiments.circle2;

public interface NodeCtrl extends Node{
	
	void leave();

	void setStandalone();
	
	void connectTo(Node other);

}
