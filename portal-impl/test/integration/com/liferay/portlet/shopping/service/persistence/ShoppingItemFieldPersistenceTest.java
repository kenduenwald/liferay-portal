/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.shopping.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import com.liferay.portlet.shopping.NoSuchItemFieldException;
import com.liferay.portlet.shopping.model.ShoppingItemField;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class ShoppingItemFieldPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (ShoppingItemFieldPersistence)PortalBeanLocatorUtil.locate(ShoppingItemFieldPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		ShoppingItemField shoppingItemField = _persistence.create(pk);

		Assert.assertNotNull(shoppingItemField);

		Assert.assertEquals(shoppingItemField.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ShoppingItemField newShoppingItemField = addShoppingItemField();

		_persistence.remove(newShoppingItemField);

		ShoppingItemField existingShoppingItemField = _persistence.fetchByPrimaryKey(newShoppingItemField.getPrimaryKey());

		Assert.assertNull(existingShoppingItemField);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addShoppingItemField();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		ShoppingItemField newShoppingItemField = _persistence.create(pk);

		newShoppingItemField.setItemId(ServiceTestUtil.nextLong());

		newShoppingItemField.setName(ServiceTestUtil.randomString());

		newShoppingItemField.setValues(ServiceTestUtil.randomString());

		newShoppingItemField.setDescription(ServiceTestUtil.randomString());

		_persistence.update(newShoppingItemField, false);

		ShoppingItemField existingShoppingItemField = _persistence.findByPrimaryKey(newShoppingItemField.getPrimaryKey());

		Assert.assertEquals(existingShoppingItemField.getItemFieldId(),
			newShoppingItemField.getItemFieldId());
		Assert.assertEquals(existingShoppingItemField.getItemId(),
			newShoppingItemField.getItemId());
		Assert.assertEquals(existingShoppingItemField.getName(),
			newShoppingItemField.getName());
		Assert.assertEquals(existingShoppingItemField.getValues(),
			newShoppingItemField.getValues());
		Assert.assertEquals(existingShoppingItemField.getDescription(),
			newShoppingItemField.getDescription());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ShoppingItemField newShoppingItemField = addShoppingItemField();

		ShoppingItemField existingShoppingItemField = _persistence.findByPrimaryKey(newShoppingItemField.getPrimaryKey());

		Assert.assertEquals(existingShoppingItemField, newShoppingItemField);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchItemFieldException");
		}
		catch (NoSuchItemFieldException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ShoppingItemField newShoppingItemField = addShoppingItemField();

		ShoppingItemField existingShoppingItemField = _persistence.fetchByPrimaryKey(newShoppingItemField.getPrimaryKey());

		Assert.assertEquals(existingShoppingItemField, newShoppingItemField);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		ShoppingItemField missingShoppingItemField = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingShoppingItemField);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		ShoppingItemField newShoppingItemField = addShoppingItemField();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ShoppingItemField.class,
				ShoppingItemField.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("itemFieldId",
				newShoppingItemField.getItemFieldId()));

		List<ShoppingItemField> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		ShoppingItemField existingShoppingItemField = result.get(0);

		Assert.assertEquals(existingShoppingItemField, newShoppingItemField);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ShoppingItemField.class,
				ShoppingItemField.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("itemFieldId",
				ServiceTestUtil.nextLong()));

		List<ShoppingItemField> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		ShoppingItemField newShoppingItemField = addShoppingItemField();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ShoppingItemField.class,
				ShoppingItemField.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("itemFieldId"));

		Object newItemFieldId = newShoppingItemField.getItemFieldId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("itemFieldId",
				new Object[] { newItemFieldId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingItemFieldId = result.get(0);

		Assert.assertEquals(existingItemFieldId, newItemFieldId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ShoppingItemField.class,
				ShoppingItemField.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("itemFieldId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("itemFieldId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected ShoppingItemField addShoppingItemField()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		ShoppingItemField shoppingItemField = _persistence.create(pk);

		shoppingItemField.setItemId(ServiceTestUtil.nextLong());

		shoppingItemField.setName(ServiceTestUtil.randomString());

		shoppingItemField.setValues(ServiceTestUtil.randomString());

		shoppingItemField.setDescription(ServiceTestUtil.randomString());

		_persistence.update(shoppingItemField, false);

		return shoppingItemField;
	}

	private ShoppingItemFieldPersistence _persistence;
}