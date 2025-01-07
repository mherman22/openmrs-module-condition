/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.condition.api.impl;

import org.openmrs.Condition;
import org.openmrs.module.condition.api.ConditionService;
import org.openmrs.module.condition.api.dao.ConditionDao;

import java.util.List;

public class ConditionServiceImpl extends org.openmrs.api.impl.ConditionServiceImpl implements ConditionService {
	
	ConditionDao dao;
	
	public ConditionServiceImpl() {
	}
	
	public void setDao(ConditionDao dao) {
		this.dao = dao;
		super.setConditionDAO(this.dao);
	}
	
	@Override
	public List<Condition> getAllConditions() {
		return dao.getAllConditions();
	}
	
	@Override
	public List<Condition> getAllConditionsByCreator(Integer id) {
		return dao.getAllConditionsByCreator(id);
	}
}
