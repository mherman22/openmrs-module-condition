/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.condition.api;

import org.openmrs.Condition;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.openmrs.module.condition.ConditionConfig.MODULE_PRIVILEGE;

/**
 * This interface has extended method declarations of ConditionService
 *
 * @see org.openmrs.api.ConditionService
 */
public interface ConditionService extends OpenmrsService {

    @Authorized(MODULE_PRIVILEGE)
    @Transactional
    List<Condition> getAllConditions();

    /**
     * Retrieves all conditions created by a specific user.
     *
     * @param userId the ID of the creator (user)
     * @return a list of conditions created by the specified user
     */
    @Authorized(MODULE_PRIVILEGE)
    @Transactional
    List<Condition> getAllConditionsByCreator(Integer userId);

}
