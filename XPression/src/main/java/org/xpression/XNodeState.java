package org.xpression;

public enum XNodeState {
	UNINITIALIZED,	//Node instatiated
	INITIALIZED,	//Node thread started
	WAITING,		//Node waiting for input change
	EVALUATING,		//Node evaluating
	DESTROYED		//Node disposed
}
