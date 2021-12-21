
package org.egov.client.edcr;

import static org.egov.edcr.utility.DcrConstants.HEIGHTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.WRONGHEIGHTDEFINED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.SetBack;
import org.egov.edcr.feature.SetBackService;
import org.egov.edcr.utility.DcrConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetBackService_AR extends SetBackService {
	private static final Logger LOG = Logger.getLogger(SetBackService_AR.class);
    @Autowired
    private FrontYardService_AR frontYardService;

    @Autowired
    private SideYardService_AR sideYardService;

    @Autowired
    private RearYardService_AR rearYardService;

    @Override
    public Plan validate(Plan pl) {
        HashMap<String, String> errors = new HashMap<>();
        // Assumption: if height of one level, should be less than next level. this condition not validated.as in each level user
        // can define different height.
        BigDecimal heightOfBuilding = BigDecimal.ZERO;
        for (Block block : pl.getBlocks()) {
            heightOfBuilding = block.getBuilding().getBuildingHeight();
            int i = 0;
            if (!block.getCompletelyExisting()) {
                for (SetBack setback : block.getSetBacks()) {
                    i++;
                    // if height not defined other than 0 level , then throw error.
                    if (setback.getLevel() == 0) {
                        // for level 0, all the yards are mandatory. Else throw error.
                        if (setback.getFrontYard() == null)
                            errors.put("frontyardNodeDefined",
                                    getLocaleMessage(OBJECTNOTDEFINED, " SetBack of " + block.getName() + "  at level zero "));
                        if (setback.getRearYard() == null
                                && !pl.getPlanInformation().getNocToAbutRearDesc().equalsIgnoreCase(DcrConstants.YES))
                            errors.put("rearyardNodeDefined",
                                    getLocaleMessage(OBJECTNOTDEFINED, " Rear Setback of  " + block.getName() + "  at level zero "));
                        if (setback.getSideYard1() == null)
                            errors.put("side1yardNodeDefined", getLocaleMessage(OBJECTNOTDEFINED,
                                    " Side Setback 1 of block " + block.getName() + " at level zero"));
                        if (setback.getSideYard2() == null
                                && !pl.getPlanInformation().getNocToAbutSideDesc().equalsIgnoreCase(DcrConstants.YES))
                            errors.put("side2yardNodeDefined", getLocaleMessage(OBJECTNOTDEFINED,
                                    " Side Setback 2 of block " + block.getName() + " at level zero "));
                    } else if (setback.getLevel() > 0) {
                        // height defined in level other than zero must contain height
                        if (setback.getFrontYard() != null && setback.getFrontYard().getHeight() == null)
                            errors.put("frontyardnotDefinedHeight", getLocaleMessage(HEIGHTNOTDEFINED, "Front Setback ",
                                    block.getName(), setback.getLevel().toString()));
                        if (setback.getRearYard() != null && setback.getRearYard().getHeight() == null)
                            errors.put("rearyardnotDefinedHeight", getLocaleMessage(HEIGHTNOTDEFINED, "Rear Setback ",
                                    block.getName(), setback.getLevel().toString()));
                        if (setback.getSideYard1() != null && setback.getSideYard1().getHeight() == null)
                            errors.put("side1yardnotDefinedHeight", getLocaleMessage(HEIGHTNOTDEFINED, "Side Setback 1 ",
                                    block.getName(), setback.getLevel().toString()));
                        if (setback.getSideYard2() != null && setback.getSideYard2().getHeight() == null)
                            errors.put("side2yardnotDefinedHeight", getLocaleMessage(HEIGHTNOTDEFINED, "Side Setback 2 ",
                                    block.getName(), setback.getLevel().toString()));
                    }

                    // if height of setback greater than building height ?
                    // last level height should match with building height.

                    if (setback.getLevel() > 0 && block.getSetBacks().size() == i) {
                        if (setback.getFrontYard() != null && setback.getFrontYard().getHeight() != null
                                && setback.getFrontYard().getHeight().compareTo(heightOfBuilding) != 0)
                            errors.put("frontyardDefinedWrongHeight", getLocaleMessage(WRONGHEIGHTDEFINED, "Front Setback ",
                                    block.getName(), setback.getLevel().toString(), heightOfBuilding.toString()));
                        if (setback.getRearYard() != null && setback.getRearYard().getHeight() != null
                                && setback.getRearYard().getHeight().compareTo(heightOfBuilding) != 0)
                            errors.put("rearyardDefinedWrongHeight", getLocaleMessage(WRONGHEIGHTDEFINED, "Rear Setback ",
                                    block.getName(), setback.getLevel().toString(), heightOfBuilding.toString()));
                        if (setback.getSideYard1() != null && setback.getSideYard1().getHeight() != null
                                && setback.getSideYard1().getHeight().compareTo(heightOfBuilding) != 0)
                            errors.put("side1yardDefinedWrongHeight", getLocaleMessage(WRONGHEIGHTDEFINED, "Side Setback 1 ",
                                    block.getName(), setback.getLevel().toString(), heightOfBuilding.toString()));
                        if (setback.getSideYard2() != null && setback.getSideYard2().getHeight() != null
                                && setback.getSideYard2().getHeight().compareTo(heightOfBuilding) != 0)
                            errors.put("side2yardDefinedWrongHeight", getLocaleMessage(WRONGHEIGHTDEFINED, "Side Setback 2 ",
                                    block.getName(), setback.getLevel().toString(), heightOfBuilding.toString()));
                    }
                }
            }
        }
        if (errors.size() > 0)
            pl.addErrors(errors);

        return pl;
    }

    @Override
    public Plan process(Plan pl) {
    	try {
            validate(pl);
            
    		BigDecimal depthOfPlot = pl.getPlanInformation().getDepthOfPlot();
    		if (depthOfPlot != null && depthOfPlot.compareTo(BigDecimal.ZERO) > 0) {
    			frontYardService.processFrontYard(pl);
    			rearYardService.processRearYard(pl);
    		}

    		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
    		if (widthOfPlot != null && widthOfPlot.compareTo(BigDecimal.ZERO) > 0) {
    			sideYardService.processSideYard(pl);
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}


        return pl;
    }

    @Override
    public Map<String, Date> getAmendments() {
        return new LinkedHashMap<>();
    }
}
