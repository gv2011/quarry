package com.github.gv2011.experiments.circle2;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeTest {

	private static final Logger LOG = LoggerFactory.getLogger(NodeTest.class);

	private Factory factory;
	
	private NodeCtrl n2;
	private NodeCtrl n3;
	private NodeCtrl n1;

	@Test
	public void testGetResponsable() {
		createCircle(new Factory(true));
		Position p = position(0x11);
		assertThat(n1.getResponsible(p), is(n1));
		assertThat(n2.getResponsible(p), is(n1));
		assertThat(n3.getResponsible(p), is(n1));
	}

	@Test
	public void testGetResponsableEfficiencyWithoutCache() throws IOException {
		createCircle(new Factory(false));
		assertThat(testGetResponsableEfficiencyInt(), is(228));
	}

	@Test
	public void testGetResponsableEfficiency() throws IOException {
		createCircle(new Factory(true));
		assertThat(testGetResponsableEfficiencyInt(), is(4));
	}

	/**
	 * @return number of recursions needed to find the responsible for a position.
	 */
	private int testGetResponsableEfficiencyInt() throws IOException {
		List<NodeCtrl> nodes = enterMany();
		NodeCtrl n = nodes.iterator().next();
		int count = Integer.MAX_VALUE-1;
		int countBefore = Integer.MAX_VALUE;
		while(count<countBefore){
			AtomicInteger counter = new AtomicInteger();
			n1.getResponsible(n.position(), counter);
			countBefore = count;
			count = counter.get();
			LOG.info("Needed {} recursions.", count);
		}
		return count;
	}

	@Test
	public void testEnter() {
		createCircle(new Factory(true));
		NodeCtrl n4 = node(0x40);
		n4.connectTo(n1);
		assertThat(left(n4), is(n3));
		assertThat(rigth(n4), is(n1));
		
		assertThat(((NodeImp)n1).getLeft().get(), is(n4));
		assertThat(rigth(n3), is(n4));
	}

	private Node left(Node node) {
		return ((NodeImp)node).getLeft().get();
	}

	private Node rigth(Node node) {
		return ((NodeImp)node).getRight().get();
	}

	@Test
	public void testLeave() throws IOException {
		createCircle(new Factory(true));
		List<NodeCtrl> nodes = enterMany();
		for(NodeCtrl n: nodes){
			n.leave();
			Node responsable = n1.getResponsible(n.position());
			assertThat(responsable, not(is(n)));
		}
		assertThat(left(n1), is(n3));
		assertThat(rigth(n1), is(n2));
		
		assertThat(left(n2), is(n1));
		assertThat(rigth(n2), is(n3));
		
		assertThat(left(n3), is(n2));
		assertThat(rigth(n3), is(n1));
	}

	@Test
	public void testEnterMany() throws IOException {
		createCircle(new Factory(true));
		List<NodeCtrl> nodes = enterMany();
		for(NodeCtrl n: nodes){
			assertThat(n1.getResponsible(n.position()), is(n));
		}
		System.out.println(((NodeImp)n1).cache);
	}

	//	@Test
	public void testEnterVeryMany() throws IOException {
		createCircle(new Factory(true));
		List<NodeCtrl> nodes = new ArrayList<>();
		for(int i=0; i<1000; i++){
			NodeCtrl n = factory.createNode();
			nodes.add(n);
			n.connectTo(n1);
		}
		for(Node n: nodes){
			assertThat(n1.getResponsible(n.position()), is(n));
		}
		System.out.println(((NodeImp)n1).cache);
	}

	private List<NodeCtrl> enterMany() throws IOException {
		List<NodeCtrl> nodes = new ArrayList<>();
		try(BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("ids.txt")))){
			String line = r.readLine();
			while(line!=null){
				NodeCtrl n = factory.createNode(new Position(UUID.fromString(line)));
				nodes.add(n);
				n.connectTo(n1);
				line = r.readLine();
			}
		}
		return nodes;
	}

	private void createCircle(Factory factory) {
		this.factory = factory;
		
		n1 = node(0x10);
		n2 = node(0x20);
		n3 = node(0x30);
		
		n1.setStandalone();		
		n2.connectTo(n1);
		n3.connectTo(n1);
	}

	private NodeCtrl node(long i) {
		return factory.createNode(position(i));
	}

	private Position position(long i) {
		return new Position(BigInteger.valueOf(i));
	}

}
