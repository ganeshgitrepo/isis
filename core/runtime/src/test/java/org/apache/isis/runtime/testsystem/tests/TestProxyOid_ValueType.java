package org.apache.isis.runtime.testsystem.tests;

import java.util.Arrays;
import java.util.List;

import org.apache.isis.runtime.testsystem.TestProxyOid;
import org.apache.isis.testsupport.ValueTypeContractTestAbstract;

public class TestProxyOid_ValueType extends ValueTypeContractTestAbstract<TestProxyOid> {

	@Override
	protected List<TestProxyOid> getObjectsWithSameValue() {
		return Arrays.asList(new TestProxyOid(1, true), new TestProxyOid(1, true));
	}

	@Override
	protected List<TestProxyOid> getObjectsWithDifferentValue() {
		return Arrays.asList(new TestProxyOid(1, false), new TestProxyOid(2, true));
	}


}
