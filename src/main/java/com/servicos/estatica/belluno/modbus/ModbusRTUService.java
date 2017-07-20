package com.servicos.estatica.belluno.modbus;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteCoilRequest;
import com.ghgande.j2mod.modbus.msg.WriteMultipleCoilsRequest;
import com.ghgande.j2mod.modbus.msg.WriteSingleRegisterRequest;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class ModbusRTUService {

	private static SerialConnection con;
	private static SerialParameters params;
	private static ModbusSerialTransaction trans;
	// private static ReadMultipleRegistersRequest req;
	private static ReadMultipleRegistersResponse res;
	private static ReadCoilsResponse coilsResponse;
	// private static ModbusRequest modReq;

	private static ModbusRequest readMultipleRegistersRequest;
	private static ModbusRequest writeSingleRegister;
	private static ModbusRequest writeCoilRequest;
	private static ModbusRequest writeMultipleCoils;
	private static ModbusRequest readCoil;

	public SerialPort[] getComPorts() {
		return SerialPort.getCommPorts();
	}

	public void setConnectionParams(String portname, int baudrate) {
		params = new SerialParameters();
		params.setPortName(portname);
		params.setBaudRate(baudrate);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("RTU");
		con = new SerialConnection(params);
	}

	public void openConnection() {
		if (!con.isOpen()) {
			con.open();
		}
	}

	public void closeConnection() {
		if (con.isOpen()) {
			con.close();
		}
	}

	public Double readMultipleRegisters(int unitId, int register, int quantity) {
		readMultipleRegistersRequest = new ReadMultipleRegistersRequest(register, quantity);
		readMultipleRegistersRequest.setUnitID(unitId);
		readMultipleRegistersRequest.setHeadless();
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(readMultipleRegistersRequest);
		try {
			trans.execute();
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
		res = (ReadMultipleRegistersResponse) trans.getResponse();
		return new Double(res.getRegisterValue(0) / (double) 10);
	}

	public Boolean readCoil(int unitId, int register, int quantity) {
		readCoil = new ReadCoilsRequest(register, quantity);
		readCoil.setUnitID(unitId);
		readCoil.setHeadless();
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(readCoil);
		try {
			trans.execute();
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
		coilsResponse = (ReadCoilsResponse) trans.getResponse();
		return coilsResponse.getCoils().getBit(0);
	}

	public void writeSingleRegister(int unitId, int register, int val) {
		writeSingleRegister = new WriteSingleRegisterRequest(register, new SimpleRegister(val));
		writeSingleRegister.setUnitID(unitId);
		writeSingleRegister.setHeadless();
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(writeSingleRegister);
		try {
			trans.execute();
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
	}

	public void writeCoilRequest(int unitId, int ref, boolean b) {
		writeCoilRequest = new WriteCoilRequest(ref, b);
		writeCoilRequest.setUnitID(unitId);
		writeCoilRequest.setHeadless();
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(writeCoilRequest);
		try {
			trans.execute();
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
	}

	public void writeMultipleCoils(int unitId, int ref, BitVector vector) {
		writeMultipleCoils = new WriteMultipleCoilsRequest(ref, vector);
		writeMultipleCoils.setUnitID(unitId);
		writeMultipleCoils.setHeadless();
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(writeMultipleCoils);
		try {
			trans.execute();
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
	}
}
