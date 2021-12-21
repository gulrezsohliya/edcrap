package org.egov.client.edcr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.SetBack;
import org.egov.edcr.feature.PlanInfoFeature;
import org.springframework.stereotype.Service;

@Service
public class PlanInfoFeature_AR extends PlanInfoFeature	{

	private static final Logger LOG = Logger.getLogger(PlanInfoFeature_AR.class);
	
	@Override
    public Plan validate(Plan pl) {
        // TODO Auto-generated method stub
        return pl;
    }

    @Override
    public Plan process(Plan pl) {
    	
		/*
		 * for(Map.Entry<String, String> entry : pl.getPlanInfoProperties().entrySet())
		 * { LOG.info("key = " + entry.getKey()+ " value = " + entry.getValue()); }
		 */
    	removeDefaultErrorMsg(pl);
    	
        return pl;
    }

    public void removeDefaultErrorMsg (Plan pl) {
		/*
		 * LOG.info("Errors"); pl.getErrors().entrySet().forEach(System.out::println);
		 */
    	
    	if(pl.getErrors() != null && !pl.getErrors().isEmpty()) {
    	
			String setbackErrorKey = "BLK_%s_LVL_%s_%s_SETBACK";
			String stairErrorKey = "BLK_%s_FLR_%s_STAIR";
			Set<String> listErrorKeys = new HashSet<String>();
			
			if(pl.getBlocks() != null && !pl.getBlocks().isEmpty()) {
				for(Block bl : pl.getBlocks()) {
					for (SetBack setback : bl.getSetBacks()) {

						if(setback.getFrontYard() == null )
							listErrorKeys.add(String.format(setbackErrorKey, bl.getNumber(), setback.getLevel(), "FRONT"));
						if(setback.getRearYard() == null )
							listErrorKeys.add(String.format(setbackErrorKey, bl.getNumber(), setback.getLevel(), "REAR"));
						if(setback.getSideYard1() == null )
							listErrorKeys.add(String.format(setbackErrorKey, bl.getNumber(), setback.getLevel(), "SIDE1"));
						if(setback.getSideYard2() == null )
							listErrorKeys.add(String.format(setbackErrorKey, bl.getNumber(), setback.getLevel(), "SIDE2"));
					}
					if(bl.getBuilding() != null && bl.getBuilding().getFloors() != null && !bl.getBuilding().getFloors().isEmpty()) {
						for(Floor fl : bl.getBuilding().getFloors()) {
							listErrorKeys.add(String.format(stairErrorKey, bl.getNumber(), fl.getNumber()));
						}
					}
				}
			}
			
			for(String t: listErrorKeys) {
				pl.getErrors().entrySet().removeIf(entry -> entry.getValue().contains(t));
				pl.getErrors().entrySet().removeIf(entry -> entry.getKey().contains(t));
			}
		}
    }
}
