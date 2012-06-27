package br.com.dextra.comercial.muuk.web.opportunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import br.com.dextra.comercial.muuk.domain.sales.Opportunity;
import br.com.dextra.comercial.muuk.domain.sales.OpportunityBusinessExecutiveComparator;
import br.com.dextra.comercial.muuk.domain.sales.OpportunityRepository;
import br.com.dextra.common.json.JSONObject;

@Path("/opportunity")
public class OpportunityRS {

	@EJB(name = OpportunityRepository.JNDI_LOCAL)
	private OpportunityRepository repository;

	@GET
	@Path("/all/{status}")
	public String findOpportunitiesByStatus(@PathParam("status") String status) {
		List<Opportunity> opportunitiesList = repository.findByStatus(status);
		Collections.sort(opportunitiesList, new OpportunityBusinessExecutiveComparator());

		return new JSONObject(opportunitiesList).toJson();
	}

	@GET
	@Path("/all/muuk-states")
	public String findOpportunitiesMuukStates() {
		Map<String, List<Opportunity>> opportunitiesMap = new HashMap<String, List<Opportunity>>();

		List<Opportunity> opportunitiesList = repository.findByStatus(this.getMuukStates());
		for (Opportunity opportunity : opportunitiesList) {
			String opportunityStatus = opportunity.getStatusCode();

			List<Opportunity> opportunitiesByStatus = null;
			if (opportunitiesMap.containsKey(opportunityStatus)) {
				opportunitiesByStatus = opportunitiesMap.get(opportunityStatus);
			} else {
				opportunitiesByStatus = new ArrayList<Opportunity>();
			}

			opportunitiesByStatus.add(opportunity);
			opportunitiesMap.put(opportunityStatus, opportunitiesByStatus);
		}

		Set<String> opportunitiesMapKeys = opportunitiesMap.keySet();
		for (String opportunityMapKey : opportunitiesMapKeys) {
			Collections.sort(opportunitiesMap.get(opportunityMapKey), new OpportunityBusinessExecutiveComparator());
		}

		return new JSONObject(opportunitiesList).toJson();
	}

	private List<String> getMuukStates() {
		List<String> muukStates = new ArrayList<String>();
		muukStates.add("EmExecucao");
		muukStates.add("PropostaAprovacao");
		return muukStates;
	}

}