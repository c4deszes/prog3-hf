<?xml version="1.0" encoding="UTF-8"?>
<xsheet name="StdlibTest">
	<!--
	<xconnection from="Value1.Output" to="Adder.A" />
	<xconnection from="Value2.Output" to="Adder.B" />
	<xconnection from="Adder.Output" to="Result1.Input" />
	-->
	
	<xref id="Value1" reference="Value" x="20" y="40">
		<param type="java.lang.Integer" value="1000" />
	</xref>
	
	<xref id="Value2" reference="Value" x="20" y="200" wrapper="">
		<param type="java.lang.Integer" value="40" />
	</xref>
	
	<xref id="Adder" reference="Math" x="400" y="70">
		<param type="java.lang.Class" value="java.lang.Integer" />
		<param type="java.lang.Class" value="java.lang.Integer" />
		<param type="org.xpression.math.Operation" value="ADD" />
	</xref>
	
	<xref id="Result1" reference="Result" x="700" y="100">
		<param type="java.lang.Class" value="java.lang.Integer" />
	</xref>
	
	<xconnection from="True.Output" to="AND.A" />
	<xconnection from="SquareWave.Output" to="AND.B" />
	
	<xref id="SquareWave" reference="Pulse" x="200" y="500">
		<input input="OnTime" value="500" />
		<input input="OffTime" value="500" />
		<input input="On" value="true" />
		<input input="Off" value="false" />
		<param type="java.lang.Class" value="java.lang.Boolean" />
	</xref>
	
	<xref id="True" reference="Value" x="200" y="300">
		<param type="java.lang.Boolean" value="true" />
	</xref>
	
	<xref id="AND" reference="Boole" x="500" y="400" >
		<param type="org.xpression.logic.Gate" value="AND" />
		<param type="java.lang.Integer" value="2" />
	</xref>
	
	<xref id="Result2" reference="Result" x="700" y="400">
		<param type="java.lang.Class" value="java.lang.Boolean" />
	</xref>
	
	<xconnection from="AND.Output" to="Result2.Input" />
</xsheet>