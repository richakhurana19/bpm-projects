package org.jbpm.process.migration.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.process.migration.MigrationManager;
import org.jbpm.process.migration.NodeMapping;
import org.jbpm.process.migration.ProcessData;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class PerformMigrationHandler implements WorkItemHandler {

	@SuppressWarnings("unchecked")
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ProcessData data = (ProcessData) workItem.getParameter("in_processdata");
//		List<NodeMapping> nodeMapping = (List<NodeMapping>) workItem.getParameter("in_nodemapping");
		List<NodeMapping> nodeMapping = new ArrayList<NodeMapping>();
		Map<String, Map<String, String>> inNodeMapping = (Map<String, Map<String, String>>) workItem.getParameter("in_nodemapping");
		
		for (Map<String, String> inputMapping : inNodeMapping.values()) {
			nodeMapping.add(new NodeMapping(inputMapping.get("mapping_sourceNodeId"), inputMapping.get("mapping_targetNodeId")));
		}
		
		
		
		String outcome = MigrationManager.migrate(data, nodeMapping);
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("out_outcome", outcome);
		
		manager.completeWorkItem(workItem.getId(), results);
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// no-op

	}

}
