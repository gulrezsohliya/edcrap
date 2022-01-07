package org.egov.client.edcr;
import static org.egov.client.constants.DxfFileConstants_AR.*;
import static org.egov.edcr.constants.DxfFileConstants.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.BuildingHeight;
import org.egov.edcr.utility.DcrConstants;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class BuildingHeight_AR extends BuildingHeight {
	private static final Logger LOG = Logger.getLogger(BuildingHeight_AR.class);
	public static final BigDecimal HEIGHT_AR_04_5 = BigDecimal.valueOf(4.5);
	public static final BigDecimal HEIGHT_AR_06 = BigDecimal.valueOf(6);
	public static final BigDecimal HEIGHT_AR_09 = BigDecimal.valueOf(9);
	public static final BigDecimal HEIGHT_AR_12 = BigDecimal.valueOf(12);
	public static final BigDecimal HEIGHT_AR_14 = BigDecimal.valueOf(14);
	public static final BigDecimal HEIGHT_AR_15 = BigDecimal.valueOf(15);
	public static final BigDecimal HEIGHT_AR_18 = BigDecimal.valueOf(18);
	public static final BigDecimal HEIGHT_AR_20 = BigDecimal.valueOf(20);
	public static final BigDecimal HEIGHT_AR_25 = BigDecimal.valueOf(25);
	public static final BigDecimal HEIGHT_AR_26 = BigDecimal.valueOf(26);
	public static final BigDecimal HEIGHT_AR_30 = BigDecimal.valueOf(30);
	public static final BigDecimal HEIGHT_AR_37 = BigDecimal.valueOf(37);
	public static final BigDecimal HEIGHT_AR_50 = BigDecimal.valueOf(50);
	public static final BigDecimal HEIGHT_AR_8_4 = BigDecimal.valueOf(8.4);
	public static final BigDecimal HEIGHT_AR_14_4 = BigDecimal.valueOf(14.4);
	public static final BigDecimal HEIGHT_AR_17_4 = BigDecimal.valueOf(17.4);
	
	// AREA for AP
	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_5000 = BigDecimal.valueOf(5000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_28000 = BigDecimal.valueOf(28000);
	private static final BigDecimal PLOT_AREA_6000 = BigDecimal.valueOf(6000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(1080);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(510);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);
	
	private static final BigDecimal PLOT_WIDTH_8 = BigDecimal.valueOf(8);

	private static final String RULE_DESCRIPTION = "Maximum building height allowed";
	private static final String RULE_TBD = "Rule TBD";
	private static final String BUILDING_HEIGHT = "Building Height";

	@Override
	public Plan validate(Plan plan) {
		String OccupancyType = plan.getPlanInformation().getLandUseZone().toUpperCase();
		HashMap<String, String> errors = new HashMap<>();
		BigDecimal plotArea = BigDecimal.ZERO, plotWidth = BigDecimal.ZERO;
		plotArea = plan.getPlanInformation().getPlotArea();
		plotWidth = plan.getPlanInformation().getWidthOfPlot();
		if(plotArea.compareTo(BigDecimal.ZERO) <= 0) {
			errors.put("PLOT_AREA_ERROR", "Plot area is less than 0");
		}
		if(plotWidth.compareTo(BigDecimal.ZERO) <= 0) {
			errors.put("PLOT_WIDTH_ERROR", "Plot width is less than 0");
		}
		if(!errors.isEmpty()) {
			plan.addErrors(errors);
			LOG.error("Required parameters are not defined.");
			return plan;
		}
		
		scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.setKey("Common_Height of Building");
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		scrutinyDetail.addColumnHeading(3, OCCUPANCY);
		scrutinyDetail.addColumnHeading(4, PERMISSIBLE); 
		scrutinyDetail.addColumnHeading(5, PROVIDED);
		scrutinyDetail.addColumnHeading(6, STATUS);

		for (Block block : plan.getBlocks()) {
			List<OccupancyTypeHelper> plotWiseOccupancyTypes = new ArrayList<>();
			for (Occupancy occupancy : block.getBuilding().getOccupancies()) {
				if (occupancy.getTypeHelper().getType() != null) {
					if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode() != null
							&& !occupancy.getTypeHelper().getType().getCode().isEmpty()
							&& !occupancy.getTypeHelper().getType().getName().isEmpty()
							&& occupancy.getTypeHelper().getType().getName() != null)
						// plotWiseOccupancyTypes.add(occupancy.getTypeHelper());
						LOG.info("Occupancy code :::" + occupancy.getTypeHelper().getType().getCode());
					LOG.info("Occupancy name:::" + occupancy.getTypeHelper().getType().getName());
				}
				boolean isAccepted = false;
				String ruleNo = RULE_TBD;

				if (block.getBuilding() != null && (block.getBuilding().getBuildingHeight() != null
						|| block.getBuilding().getBuildingHeight().compareTo(BigDecimal.ZERO) > 0)) {

					String requiredBuildingHeight = StringUtils.EMPTY;
					BigDecimal buildingHeight = block.getBuilding().getBuildingHeight();
					BigDecimal parapetHeight = BigDecimal.ZERO;
					BigDecimal maxHeight = BigDecimal.ZERO;
					BigDecimal stiltHeight = BigDecimal.ZERO;
										
					// add parapet height
					if (block.getParapets() != null && !block.getParapets().isEmpty()) {
						maxHeight = block.getParapets().stream().reduce(BigDecimal::max).get();
						parapetHeight = maxHeight;
					}

					// add stilts height
					for (Floor floor : block.getBuilding().getFloors()) {
						if (floor.getParking().getStilts() != null && !floor.getParking().getStilts().isEmpty()) {
							stiltHeight = stiltHeight.add(floor.getParking().getStilts().stream()
									.map(Measurement::getHeight).reduce(BigDecimal.ZERO, BigDecimal::add));
						}
					}
					
					//Adding parapet and stilt height to building height
					buildingHeight = buildingHeight.add(parapetHeight);
					buildingHeight = buildingHeight.add(stiltHeight);

					if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(A)) {
						//Residential
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_R)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(A_AF)) {
							if (plotArea.compareTo(PLOT_AREA_48) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_48) >= 0 && plotArea.compareTo(PLOT_AREA_60) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_60) >= 0 && plotArea.compareTo(PLOT_AREA_100) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_100) >= 0
									&& plotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";	
								}
							} else if (plotArea.compareTo(PLOT_AREA_250) >= 0
									&& plotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_500) >= 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1000) >= 0
									&& plotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1500) >= 0
									&& plotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_RH)) {
							if(plotWidth.compareTo(PLOT_WIDTH_8) >= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
									requiredBuildingHeight = "<= 9m";
								} 	
							}else {
								requiredBuildingHeight = "Minimum required Plot Width is 8m";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_FH)) {
							if (plotArea.compareTo(PLOT_AREA_10000) >= 0 && plotArea.compareTo(PLOT_AREA_20000) < 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6m";								
							} else if (plotArea.compareTo(PLOT_AREA_20000) >= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6m";
							}	
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_HE)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(A_BH)) {
							 if (plotArea.compareTo(PLOT_AREA_100) >= 0
									&& plotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_250) >= 0
									&& plotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_500) >= 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1000) >= 0
									&& plotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						} else if(occupancy.getTypeHelper().getSubtype().getCode().equals(A_LH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(A_GH)) {
							 if (plotArea.compareTo(PLOT_AREA_1500) >= 0 && plotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(F)) {
						//Commercial
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_RT)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_SH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_CB)) {
							if (plan.getPlot().getArea().compareTo(PLOT_AREA_48) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_100) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_100) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_250) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_250) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_500) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_1000) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_1000) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_1500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_1500) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_3000) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_3000) > 0) {
								plan.addError("PLOT AREA", "Validation for plot area > 3000 not provided");
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_SM)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_18) <= 0;
							requiredBuildingHeight = "<= 18m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_H)) { 
							// Hotel
							if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							} else if (plotArea.compareTo(PLOT_AREA_500) > 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (plotArea.compareTo(PLOT_AREA_1000) > 0
									&& plotArea.compareTo(PLOT_AREA_1500) <= 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (plotArea.compareTo(PLOT_AREA_1500) > 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_M)) {
							isAccepted = false;
							requiredBuildingHeight = "Permissible value not provided";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_R)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_PTP)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(F_PTP_FCSS)) {
								if (plotArea.compareTo(PLOT_AREA_1080) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							}
							if (occupancy.getTypeHelper().getUsage().getCode().equals(F_PTP_FS)) {
								if (plotArea.compareTo(PLOT_AREA_510) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_WH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_WST)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15";
						}

					}
					//mixed land use to be determined 
					else if (occupancy.getTypeHelper().getType() != null
							&& (occupancy.getTypeHelper().getType().getCode().equals(ML_A)
									|| occupancy.getTypeHelper().getType().getCode().equals(ML_F))) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_RT)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_SH)) {
							if (plan.getPlot().getArea().compareTo(PLOT_AREA_48) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_100) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_100) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_250) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_250) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_500) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_1000) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_1000) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_1500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_1500) > 0
									&& plan.getPlot().getArea().compareTo(PLOT_AREA_3000) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<=  17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plan.getPlot().getArea().compareTo(PLOT_AREA_3000) > 0) {
								plan.addError("PLOT AREA", "Validation for plot area > 3000 not provided");
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_SM)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_18) <= 0;
							requiredBuildingHeight = "<= 18m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_H)) {
							if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							} else if (plotArea.compareTo(PLOT_AREA_500) > 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (plotArea.compareTo(PLOT_AREA_1000) > 0
									&& plotArea.compareTo(PLOT_AREA_1500) <= 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (plotArea.compareTo(PLOT_AREA_1500) > 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_M)) {
							isAccepted = false;
							requiredBuildingHeight = "Permissible value not provided";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_R)) {
							isAccepted = false;
							requiredBuildingHeight = "Permissible value not provided";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_PTP)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(ML_F_PTP_FCSS)) {
								if (plotArea.compareTo(PLOT_AREA_1080) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(ML_F_PTP_FS)) {
								if (plotArea.compareTo(PLOT_AREA_510) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_WH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_WST)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15";
						}
						// Mixed Land Use residential
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_R)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_AF)) {
							if (plotArea.compareTo(PLOT_AREA_48) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_48) >= 0 && plotArea.compareTo(PLOT_AREA_60) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8_4) <= 0;
									requiredBuildingHeight = "<= 8.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_60) >= 0 && plotArea.compareTo(PLOT_AREA_100) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_100) >= 0
									&& plotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";	
								}
							} else if (plotArea.compareTo(PLOT_AREA_250) >= 0
									&& plotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_500) >= 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1000) >= 0
									&& plotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1500) >= 0
									&& plotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_RH)) {
							if(plotWidth.compareTo(PLOT_WIDTH_8) >= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
									requiredBuildingHeight = "<= 9m";
								} 	
							}else {
								requiredBuildingHeight = "Minimum required Plot Width is 8m";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_FH)) {
							if (plotArea.compareTo(PLOT_AREA_10000) >= 0 && plotArea.compareTo(PLOT_AREA_20000) < 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6m";								
							} else if (plotArea.compareTo(PLOT_AREA_20000) >= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6m";
							}	
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_HE)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_BH)) {
							 if (plotArea.compareTo(PLOT_AREA_100) >= 0
									&& plotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14_4) <= 0;
									requiredBuildingHeight = "<= 14.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_250) >= 0
									&& plotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_500) >= 0
									&& plotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							} else if (plotArea.compareTo(PLOT_AREA_1000) >= 0
									&& plotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						} else if(occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_LH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_GH)) {
							 if (plotArea.compareTo(PLOT_AREA_1500) >= 0 && plotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17_4) <= 0;
									requiredBuildingHeight = "<= 17.4m";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m";
								}
							}
						}
					} //Industrial
					else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(G)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(G_SC)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(G_FI)) {
							if (plotArea.compareTo(PLOT_AREA_2000) >= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(G_LSI)) {
								// check for plains and hills
							if ((plan.getPlanInfoProperties().get("TERRAIN")).equals("PLAINS")) {
								if (plotArea.compareTo(PLOT_AREA_400)<= 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12";
								} else if (plotArea.compareTo(PLOT_AREA_400) > 0
										&& plotArea.compareTo(PLOT_AREA_4000) <= 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								} else if (plotArea.compareTo(PLOT_AREA_4000) > 0
										&& plotArea.compareTo(PLOT_AREA_12000) <= 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								} else if (plotArea.compareTo(PLOT_AREA_12000) > 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								}
							} else if ((plan.getPlanInfoProperties().get("TERRAIN")).equals("HILLS")) {
								if (plotArea.compareTo(PLOT_AREA_400) <=0 ) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
									requiredBuildingHeight = "<= 9m";
								} else if (plotArea.compareTo(PLOT_AREA_400) > 0
										&& plotArea.compareTo(PLOT_AREA_4000) <= 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								} else if (plotArea.compareTo(PLOT_AREA_4000) > 0
										&& plotArea.compareTo(PLOT_AREA_12000) <= 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m";
								} else if (plotArea.compareTo(PLOT_AREA_12000) > 0) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
									requiredBuildingHeight = "<= 9m";
								}
							} else {
								plan.addError("TERRAIN TYPE", "Terrain Type should be PLAINS/HILLS");
							}
						} 
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(T)) {
						if(occupancy.getTypeHelper().getSubtype().getCode()!=null && occupancy.getTypeHelper().getSubtype().getCode().equals(T_R)) {
							if (buildingHeight.compareTo(HEIGHT_AR_30) <= 0) {
								isAccepted = false;
								requiredBuildingHeight = "Cannot be validated" ;
							}
						}else if(occupancy.getTypeHelper().getSubtype().getCode()!=null && occupancy.getTypeHelper().getSubtype().getCode().equals(T_I)) {
							if (buildingHeight.compareTo(HEIGHT_AR_12) <= 0) {
								isAccepted = true;
								requiredBuildingHeight = "<=12m" ;
							}
						}	
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(P)) {
						if (buildingHeight.compareTo(HEIGHT_AR_15) <= 0) {
							isAccepted = true;
							requiredBuildingHeight = "<= " + HEIGHT_AR_15;
						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(C)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_H)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_T)) {
							isAccepted =false;
							requiredBuildingHeight = "Cannot be validated";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_NH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_PC)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_D)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_DC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_VH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_VD)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_NAPI)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(B)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_NS)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
							requiredBuildingHeight = "<= 6m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_PS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_UPS)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_HSS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_SS)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_C)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_U)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_SFMC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) < 0;
							requiredBuildingHeight = "<= 9m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_ERIC)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_AC)) {
								if (plotArea.compareTo(BigDecimal.valueOf(0.45).multiply(plotArea)) >= 0) {
									isAccepted = true;
									requiredBuildingHeight = "As per provision of Master Plan";
								}
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_AR)) {
								if (plotArea.compareTo(BigDecimal.valueOf(0.25).multiply(plotArea)) >= 0) {
									isAccepted = true;
									requiredBuildingHeight = "As per provision of Master Plan";
								}
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_SCC)
									|| occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_POS)) {
								if (plotArea.compareTo(BigDecimal.valueOf(0.15).multiply(plotArea)) >= 0) {
									isAccepted = false;
									requiredBuildingHeight = "Cannot validate";
								}
							}
						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_SP)) {
							isAccepted = false;
							requiredBuildingHeight = "Cannot validate";
						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(U)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_PP)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_PS)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_DOB)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_DJ)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<=15m";
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_PTI)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							}
						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_DMC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";

						} else if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_FP)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FTI)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FTC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						}
					}
					
					if (errors.isEmpty() && StringUtils.isNotBlank(requiredBuildingHeight)) {
						Map<String, String> details = new HashMap<>();
						scrutinyDetail.setKey("Block_" + block.getNumber() + "_" + "Height of Building");
						details.put(RULE_NO, ruleNo);
						details.put(DESCRIPTION, RULE_DESCRIPTION);
						details.put(OCCUPANCY, occupancy.getTypeHelper().getSubtype().getName());
						details.put(PERMISSIBLE, requiredBuildingHeight);
						details.put(PROVIDED, String.valueOf(buildingHeight));
						details.put(STATUS, isAccepted ? Result.Accepted.getResultVal():Result.Not_Accepted.getResultVal());
						scrutinyDetail.getDetail().add(details);
						plan.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
					}

				} else {
					errors.put(BUILDING_HEIGHT + block.getNumber(), getLocaleMessage(DcrConstants.OBJECTNOTDEFINED,
							BUILDING_HEIGHT + " for block " + block.getNumber()));
					plan.addErrors(errors);
				}
			}
		}

		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		/*
		 * LOG.info("\nBHeight\n");
		 * LOG.info("\n====================================\n");
		 * LOG.info("ApplicantName : " + plan.getPlanInformation().getApplicantName());
		 * LOG.info("Occupancy : " + plan.getPlanInformation().getOccupancy());
		 * LOG.info("No of Blocks : " + plan.getBlocks().size());
		 * LOG.info("ApplicationDate : " + plan.getApplicationDate());
		 * LOG.info("plotArea : " + plan.getPlanInformation().getPlotArea());
		 * LOG.info("landUseZone : " + plan.getPlanInformation().getLandUseZone()); //
		 * planInformation}.getOccupancy()
		 * LOG.info("\n====================================\n");
		 */
		try {
			validate(plan);
			return plan;
		}
		catch(Exception e) {
			return plan;
		}
	}

}
