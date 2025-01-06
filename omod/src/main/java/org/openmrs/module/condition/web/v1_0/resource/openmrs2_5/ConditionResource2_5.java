package org.openmrs.module.condition.web.v1_0.resource.openmrs2_5;

import org.openmrs.Condition;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.module.condition.api.ConditionService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs2_2.ConditionResource2_2;

/**
 * {@link Resource} for Condition, supporting standard CRUD operations
 */
@Resource(name = RestConstants.VERSION_1 + "/condition", order = 2, supportedClass = Condition.class, supportedOpenmrsVersions = { "2.5.* - 9.*" })
public class ConditionResource2_5 extends ConditionResource2_2 {
	
	private ConditionService conditionService = Context.getService(ConditionService.class);
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = super.getRepresentationDescription(representation);
		if (description != null) {
			description.addProperty("chiefComplaintCount");
		}
		return description;
	}
	
	/**
	 * Searches for conditions with optional frequency-based sorting. Extends the standard OpenMRS
	 * search functionality to support sorting conditions by their frequency of occurrence across
	 * all patients.
	 * 
	 * @param context The RequestContext containing search parameters Parameter 'count': When set to
	 *            'true', returns conditions sorted by frequency
	 * @return PageableResult containing the sorted list of conditions
	 */
	@Override
	protected PageableResult doSearch(RequestContext context) {
		// Get the 'count' parameter from the request
		String sortByCount = context.getRequest().getParameter("count");
		
		if (Boolean.parseBoolean(sortByCount)) {
			// Get all conditions created by a given creator
			List<Condition> conditions = conditionService.getAllConditionsByCreator(Context.getAuthenticatedUser()
			        .getUserId());
			
			final Map<String, Integer> frequencyMap = new HashMap<String, Integer>();
			for (Condition condition : conditions) {
				String key = getConditionKey(condition);
				if (frequencyMap.containsKey(key)) {
					frequencyMap.put(key, frequencyMap.get(key) + 1);
				} else {
					frequencyMap.put(key, 1);
				}
			}
			
			List<Condition> sortedConditions = new ArrayList<Condition>(conditions);
			Collections.sort(sortedConditions, new Comparator<Condition>() {
				
				@Override
				public int compare(Condition c1, Condition c2) {
					Integer count1 = frequencyMap.get(getConditionKey(c1));
					Integer count2 = frequencyMap.get(getConditionKey(c2));
					// Sort in descending order (higher counts first)
					return count2.compareTo(count1);
				}
			});
			
			Set<String> seenKeys = new HashSet<String>();
			List<Condition> uniqueSortedConditions = new ArrayList<Condition>();
			for (Condition condition : sortedConditions) {
				String key = getConditionKey(condition);
				if (!seenKeys.contains(key)) {
					uniqueSortedConditions.add(condition);
					seenKeys.add(key);
				}
			}
			
			return new NeedsPaging<Condition>(uniqueSortedConditions, context);
		}
		
		// If count parameter is not true, delegate to standard search behavior
		return super.doSearch(context);
	}
	
	/**
	 * Generates a unique key for a condition based on its coded or non-coded value.
	 * 
	 * @param condition the condition for which the key is generated
	 * @return a unique key for the condition
	 */
	private String getConditionKey(Condition condition) {
		if (condition.getCondition().getCoded() != null) {
			return condition.getCondition().getCoded().getUuid();
		}
		return condition.getCondition().getNonCoded();
	}
	
	/**
	 * Property getter for chiefComplaintCount.
	 * 
	 * @param condition the condition for which the count is being calculated
	 * @return the count of how many times this condition is associated with a patient
	 */
	@PropertyGetter("chiefComplaintCount")
	@SuppressWarnings("unused")
	public Integer getChiefComplaintCount(Condition condition) {
		List<Condition> allConditions = conditionService.getAllConditions();
		String conditionKey = getConditionKey(condition);
		
		int count = 0;
		for (Condition c : allConditions) {
			if (getConditionKey(c).equals(conditionKey)) {
				count++;
			}
		}
		return count;
	}
}
