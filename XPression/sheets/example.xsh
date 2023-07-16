<?xml version="1.0" encoding="UTF-8"?>
<xsheet name="TestSheet">	
	<xref id="Adder" reference="Math" x="400" y="70">
		<param type="java.lang.Class" value="java.lang.Integer" />
		<param type="java.lang.Class" value="java.lang.Integer" />
		<param type="org.xpression.math.Operation" value="ADD" />
	</xref>
	
	<xref id="Wave" reference="Pulse" x="200" y="500">
		<input input="OnTime" value="500" />
		<input input="OffTime" value="500" />
		<input input="On" value="true" />
		<input input="Off" value="false" />
		<param type="java.lang.Class" value="java.lang.Boolean" />
	</xref>
	
	<xref id="True" reference="Value" x="200" y="300">
		<param type="java.lang.Boolean" value="true" />
	</xref>
	
	<xref id="And" reference="Boole" x="500" y="400" >
		<param type="org.xpression.logic.Gate" value="AND" />
		<param type="java.lang.Integer" value="2" />
	</xref>
	
	<xref id="Output" reference="Result" x="700" y="400">
		<param type="java.lang.Class" value="java.lang.Boolean" />
	</xref>
</xsheet>