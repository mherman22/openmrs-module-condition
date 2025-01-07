/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.condition.api.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Condition;
import org.openmrs.api.db.hibernate.HibernateConditionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("condition.ConditionDao")
public class ConditionDao extends HibernateConditionDAO {

    private static final String VOIDED = "voided";

    @Autowired
    SessionFactory sessionFactory;

    public ConditionDao() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        super.setSessionFactory(this.sessionFactory);
    }

    public List<Condition> getAllConditions() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Condition c where c.voided = false order by c.dateCreated desc", Condition.class).list();
    }

    public List<Condition> getAllConditionsByCreator(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Condition> query = cb.createQuery(Condition.class);
        Root<Condition> root = query.from(Condition.class);

        //where creator.userId = :userId and voided = false
        query.where(cb.and(cb.equal(root.get("creator").get("userId"), userId), cb.equal(root.get("voided"), false)))
                .orderBy(cb.desc(root.get("dateCreated")));

        return sessionFactory.getCurrentSession().createQuery(query).getResultList();
    }

}
