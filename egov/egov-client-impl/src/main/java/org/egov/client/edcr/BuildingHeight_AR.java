package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A2;

import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.A_SA;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;

import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.B2;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.B_PS;
import static org.egov.edcr.constants.DxfFileConstants.D_A;
import static org.egov.edcr.constants.DxfFileConstants.D_B;
import static org.egov.edcr.constants.DxfFileConstants.D_C;
import static org.egov.edcr.constants.DxfFileConstants.E_CLG;
import static org.egov.edcr.constants.DxfFileConstants.E_EARC;
import static org.egov.edcr.constants.DxfFileConstants.E_NS;
import static org.egov.edcr.constants.DxfFileConstants.E_PS;
import static org.egov.edcr.constants.DxfFileConstants.E_SACA;
import static org.egov.edcr.constants.DxfFileConstants.E_SFDAP;
import static org.egov.edcr.constants.DxfFileConstants.E_SFMC;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;
import static org.egov.edcr.constants.DxfFileConstants.F_H;
import static org.egov.edcr.constants.DxfFileConstants.G;

import static org.egov.edcr.constants.DxfFileConstants.H_PP;
import static org.egov.edcr.constants.DxfFileConstants.M_DFPAB;
import static org.egov.edcr.constants.DxfFileConstants.M_HOTHC;
import static org.egov.edcr.constants.DxfFileConstants.M_NAPI;
import static org.egov.edcr.constants.DxfFileConstants.M_OHF;
import static org.egov.edcr.constants.DxfFileConstants.M_VH;
import static org.egov.edcr.constants.DxfFileConstants.S_BH;
import static org.egov.edcr.constants.DxfFileConstants.S_CA;
import static org.egov.edcr.constants.DxfFileConstants.S_CRC;
import static org.egov.edcr.constants.DxfFileConstants.S_ECFG;
import static org.egov.edcr.constants.DxfFileConstants.S_ICC;
import static org.egov.edcr.constants.DxfFileConstants.S_MCH;
import static org.egov.edcr.constants.DxfFileConstants.S_SAS;
import static org.egov.edcr.constants.DxfFileConstants.S_SC;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
//from  DxfFileConstants_AR
import static org.egov.client.constants.DxfFileConstants_AR.*;

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
import org.jfree.util.Log;
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
	public static final BigDecimal HEIGHT_AR_8POINT4 = BigDecimal.valueOf(8.4);
	public static final BigDecimal HEIGHT_AR_14POINT4 = BigDecimal.valueOf(14.4);
	public static final BigDecimal HEIGHT_AR_17POINT4 = BigDecimal.valueOf(17.4);

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

	private static final String HEIGHT_BUILDING = "Maximum height of building allowed...";
	private static final String ZONE = "Zone";
	private static final String RULE_TBD = "Zone";
	private static final String BUILDING_HEIGHT = "Building Height";

	@Override
	public Plan validate(Plan pl) {


		// String OccupancyType=new Util_JK().getOccupancieType(pl);
		String OccupancyType = pl.getPlanInformation().getLandUseZone().toUpperCase();
		scrutinyDetail = new ScrutinyDetail();
		// scrutinyDetail.setKey("Common_Height of Building");
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		// scrutinyDetail.addColumnHeading(3, ZONE);
		scrutinyDetail.addColumnHeading(5, PERMISSIBLE);
		scrutinyDetail.addColumnHeading(6, PROVIDED);
		scrutinyDetail.addColumnHeading(7, STATUS);

		HashMap<String, String> errors = new HashMap<>();

		BigDecimal PlotArea = pl.getPlanInformation().getPlotArea();
		BigDecimal PlotWidth = pl.getPlanInformation().getWidthOfPlot();
		for (Block block : pl.getBlocks()) {
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
					LOG.info("occupancy.getTypeHelper().getSubtype().getCode()"+ occupancy.getTypeHelper().getSubtype().getCode());
				}
				// }
				boolean isAccepted = false;
				String ruleNo = RULE_TBD;

				scrutinyDetail.setKey("Block_" + block.getNumber() + "_" + "Height of Building");

				if (block.getBuilding() != null && (block.getBuilding().getBuildingHeight() != null
						|| block.getBuilding().getBuildingHeight().compareTo(BigDecimal.ZERO) > 0)) {

					String requiredBuildingHeight = StringUtils.EMPTY;
					BigDecimal buildingHeight = block.getBuilding().getBuildingHeight();
					LOG.info("buildingHeight w/o parapet and stilt:" + buildingHeight);
					BigDecimal ParapetHeight = BigDecimal.ZERO;
					BigDecimal maxHeight = BigDecimal.ZERO;// what is max parapet height
					BigDecimal stiltHeight = BigDecimal.ZERO;// what is max parapet height

					// add parapet height
					if (block.getParapets() != null && !block.getParapets().isEmpty()) {
						maxHeight = block.getParapets().stream().reduce(BigDecimal::max).get();
						// if (maxHeight.compareTo(ParapetHeight_1_5) > 0)
						// {
						ParapetHeight = maxHeight;
						// }
					}

					// add stilts height
					for (Floor floor : block.getBuilding().getFloors()) {
						if (floor.getParking().getStilts() != null && !floor.getParking().getStilts().isEmpty()) {
							stiltHeight = stiltHeight.add(floor.getParking().getStilts().stream()
									.map(Measurement::getHeight).reduce(BigDecimal.ZERO, BigDecimal::add));
						}

					}

					LOG.info("Parapet Height:" + ParapetHeight);
					LOG.info("Stilt Height:" + stiltHeight);
					buildingHeight = buildingHeight.add(ParapetHeight);
					buildingHeight = buildingHeight.add(stiltHeight);
					LOG.info("buildingHeight w parapet and stilt :" + buildingHeight);
					// Get Zone BigDecimal ZONE_DC = BigDecimal.valueOf(4);

					if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(A)) {

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_R)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(A_AF)) {
							 if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_60) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8POINT4) <= 0;
									requiredBuildingHeight = "<= 8.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_60) >= 0 && PlotArea.compareTo(PLOT_AREA_100) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14POINT4) <= 0;
									requiredBuildingHeight = "<= 14.4";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_100) >= 0
									&& PlotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14POINT4) <= 0;
									requiredBuildingHeight = "<= 14.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12";

								}
							} else if (PlotArea.compareTo(PLOT_AREA_250) >= 0
									&& PlotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<= 17.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15";

								}
							} else if (PlotArea.compareTo(PLOT_AREA_500) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<= 17.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15";

								}
							} else if (PlotArea.compareTo(PLOT_AREA_1000) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<= 17.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15";

								}
							} else if (PlotArea.compareTo(PLOT_AREA_1500) >= 0
									&& PlotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<= 17.4";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15";

								}

							} else {
								isAccepted = false;
								LOG.info("Required Building Height :1: ");
							}

						} if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_RH)) {
							if (PlotWidth.compareTo(PLOT_WIDTH_8) >= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
									requiredBuildingHeight = "<= 9";
								}
							} else {
								isAccepted = false;
								requiredBuildingHeight = "Minimum required Plot Width 8M";
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_FH)) {
							if (PlotArea.compareTo(PLOT_AREA_10000) >= 0 && PlotArea.compareTo(PLOT_AREA_20000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							} else if (PlotArea.compareTo(PLOT_AREA_20000) >= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							} else {
								isAccepted = false;
								LOG.info("Required Building Height :1: ");
							}

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(A_HE)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(A_BH)) {
							if (PlotArea.compareTo(PLOT_AREA_500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_500) > 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0
									&& PlotArea.compareTo(PLOT_AREA_2000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_2000) > 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else {
								isAccepted = false;
								LOG.info("Required Building Height :1: ");
							}

						}
					} else 
					if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(F)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_RT)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_SH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_CB)) {
							if (pl.getPlot().getArea().compareTo(PLOT_AREA_48) > 0
									&& pl.getPlot().getArea().compareTo(PLOT_AREA_100) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_8POINT4) <= 0;
									requiredBuildingHeight = "<= 8.4m with stilt parking";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6m without stilt parking";
								}
							} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_100) > 0
									&& pl.getPlot().getArea().compareTo(PLOT_AREA_250) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_14POINT4) <= 0;
									requiredBuildingHeight = "<= 14.4m with stilt parking";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
									requiredBuildingHeight = "<= 12m without stilt parking";
								}
							} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_250) > 0
									&& pl.getPlot().getArea().compareTo(PLOT_AREA_500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<=  17.4m with stilt parking";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m without stilt parking";
								}
							} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_500) > 0
									&& pl.getPlot().getArea().compareTo(PLOT_AREA_1000) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<=  17.4m with stilt parking";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m without stilt parking";
								}
							} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_1000) > 0
									&& pl.getPlot().getArea().compareTo(PLOT_AREA_1500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
									requiredBuildingHeight = "<=  17.4m with stilt parking";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
									requiredBuildingHeight = "<= 15m without stilt parking";
								}
							}
						} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_1500) > 0
								&& pl.getPlot().getArea().compareTo(PLOT_AREA_3000) <= 0) {
							if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_17POINT4) <= 0;
								requiredBuildingHeight = "<=  17.4m with stilt parking";
							} else {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m without stilt parking";
							}
						} else if (pl.getPlot().getArea().compareTo(PLOT_AREA_3000) > 0) {
							pl.addError("PLOT AREA", "Validation for plot area > 3000 not provided");

						}
						// Hotel
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_H)) {
							LOG.info("inside hotel:::plot area" + PlotArea);
							if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							} else if (PlotArea.compareTo(PLOT_AREA_500) > 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								LOG.info("inside hotel 2:::plot area" + PlotArea);
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0
									&& PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";
							} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0) {
								isAccepted = true;
								requiredBuildingHeight = "No Restriction";

							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_SM)) {

							isAccepted = buildingHeight.compareTo(HEIGHT_AR_18) <= 0;
							requiredBuildingHeight = "<= 18m";

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_R)) {

							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_M)) {

							isAccepted = false;
							requiredBuildingHeight = "Permissible value not provided";

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_PTP)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(F_PTP_FCSS)) {
								if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							}
							if (occupancy.getTypeHelper().getUsage().getCode().equals(F_PTP_FS)) {
								if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
									isAccepted = false;
									requiredBuildingHeight = "Permissible value not provided";
								}
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(F_WH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(F_WST)) {

							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15";

						}

					}//mixed land use to be determined 
					else if (occupancy.getTypeHelper().getType() != null
							&& (occupancy.getTypeHelper().getType().getCode().equals(ML_A)
									&& occupancy.getTypeHelper().getType().getCode().equals(ML_F))) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_RT)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_SH)) {
							if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_100) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_100) > 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							}

						}
						// Hotel
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_H)) {
							LOG.info("inside hotel:::plot area" + PlotArea);
							if (PlotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_500) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								LOG.info("inside hotel 2:::plot area" + PlotArea);
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1000) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_M)) {

							if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							} else {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_R)) {

							if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							} else {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							}

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_PTP)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(ML_F_PTP_FCSS)) {
								if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
									if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
										isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
										requiredBuildingHeight = "<= 6";
									} else {
										isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
										requiredBuildingHeight = "<= 6";
									}
								}
							}
							if (occupancy.getTypeHelper().getUsage().getCode().equals(ML_F_PTP_FS)) {
								if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
									if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
										isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
										requiredBuildingHeight = "<= 6";
									} else {
										isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
										requiredBuildingHeight = "<= 6";
									}

								}
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_WH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_F_WST)) {

							if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							} else {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
								requiredBuildingHeight = "<= 6";
							}

						}

						// Mixed Land Use residential
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_R)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_AF)) {
							if (PlotArea.compareTo(PLOT_AREA_48) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_60) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_60) >= 0 && PlotArea.compareTo(PLOT_AREA_100) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_100) >= 0
									&& PlotArea.compareTo(PLOT_AREA_250) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_250) >= 0
									&& PlotArea.compareTo(PLOT_AREA_500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_500) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1000) >= 0
									&& PlotArea.compareTo(PLOT_AREA_1500) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1500) >= 0
									&& PlotArea.compareTo(PLOT_AREA_3000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							}

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_FH)) {
							if (PlotArea.compareTo(PLOT_AREA_10000) >= 0 && PlotArea.compareTo(PLOT_AREA_20000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							} else if (PlotArea.compareTo(PLOT_AREA_20000) >= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}

							}

						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_HE)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(ML_A_BH)) {
							if (PlotArea.compareTo(PLOT_AREA_500) <= 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_500) > 0
									&& PlotArea.compareTo(PLOT_AREA_1000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0
									&& PlotArea.compareTo(PLOT_AREA_2000) < 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							} else if (PlotArea.compareTo(PLOT_AREA_2000) > 0) {
								if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								} else {
									isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
									requiredBuildingHeight = "<= 6";
								}
							}

						}

					} 
					else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(G)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(G_SC)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(G_FI)) {
							if (PlotArea.compareTo(PLOT_AREA_2000) >= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							}

						}
					
					// check for plains and hills
					else if (occupancy.getTypeHelper().getSubtype().getCode().equals(G_LSI)) {
						if ((pl.getPlanInfoProperties().get("TERRAIN")).equals("PLAINS")) {
							if (PlotArea.compareTo(PLOT_AREA_400)<= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
								requiredBuildingHeight = "<= 12";
							} else if (PlotArea.compareTo(PLOT_AREA_400) > 0
									&& PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
								requiredBuildingHeight = "<= 12m";
							} else if (PlotArea.compareTo(PLOT_AREA_4000) > 0
									&& PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
								requiredBuildingHeight = "<= 12m";
							} else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
								requiredBuildingHeight = "<= 12m";
							}
						} else if ((pl.getPlanInfoProperties().get("TERRAIN")).equals("HILLS")) {
							if (PlotArea.compareTo(PLOT_AREA_400) <=0 ) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
								requiredBuildingHeight = "<= 9";
							} else if (PlotArea.compareTo(PLOT_AREA_400) > 0
									&& PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
								requiredBuildingHeight = "<= 9";
							} else if (PlotArea.compareTo(PLOT_AREA_4000) > 0
									&& PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
								requiredBuildingHeight = "<= 9";
							} else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) <= 0;
								requiredBuildingHeight = "<= 9";
							}
						} else {
							pl.addError("TERRAIN TYPE", "Terrain Type should be PLAINS/HILLS");
						}
					} 
					}else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(T)) {
					if(occupancy.getTypeHelper().getSubtype().getCode()!=null && occupancy.getTypeHelper().getSubtype().getCode().equals(T_R)) {
						if (buildingHeight.compareTo(HEIGHT_AR_30) <= 0) {
							isAccepted = false;
							requiredBuildingHeight = "Cannot be validated" ;
						}
//							else {
//							isAccepted = false;
//							LOG.info("Required Building Height :5: ");
//						}
					}else if(occupancy.getTypeHelper().getSubtype().getCode()!=null && occupancy.getTypeHelper().getSubtype().getCode().equals(T_I)) {
						if (buildingHeight.compareTo(HEIGHT_AR_12) <= 0) {
							isAccepted = true;
							requiredBuildingHeight = "<=12m" ;
						} else {
							isAccepted = false;
						}
					}
//						
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(P)) {
						if (buildingHeight.compareTo(HEIGHT_AR_15) <= 0) {
							isAccepted = true;
							requiredBuildingHeight = "<= " + HEIGHT_AR_15;
						} else {
							isAccepted = false;
						
						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(C)) {
						
						
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_H)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_T)) {
//							isAccepted = buildingHeight.compareTo(TWO_POINTFIVE) <= 0;
							isAccepted =false;
							requiredBuildingHeight = "Cannot be validated";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_NH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_PC)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_D)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_DC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_VH)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(C_VD)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(C_NAPI)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";

						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(B)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_NS)) {

							isAccepted = buildingHeight.compareTo(HEIGHT_AR_06) <= 0;
							requiredBuildingHeight = "<= 6m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_PS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_UPS)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_HSS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_SS)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_C)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(B_U)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						}

						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_SFMC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_09) < 0;
							requiredBuildingHeight = "<= 9m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_ERIC)) {

							if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_AC)
									|| occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_AR)) {
								if (PlotArea.compareTo(BigDecimal.valueOf(0.45).multiply(PlotArea)) >= 0) {
//									isAccepted = buildingHeight.compareTo(ONE_POINTTWO) < 0;
									isAccepted =true;
									requiredBuildingHeight = "<= As per provision of Master Plan";
								}
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_SCC)) {
								if (PlotArea.compareTo(BigDecimal.valueOf(0.15).multiply(PlotArea)) >= 0) {
									isAccepted = false;
									requiredBuildingHeight = "Cannot Validate";
								}
							} else if (occupancy.getTypeHelper().getUsage().getCode().equals(B_ERIC_POS)) {
								if (PlotArea.compareTo(BigDecimal.valueOf(0.15).multiply(PlotArea)) >= 0) {
									isAccepted = false;
									requiredBuildingHeight = "Cannot Validate";
								}
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(B_SP)) {
							isAccepted = false;
							requiredBuildingHeight = "Cannot Validate";

						}
					} else if (occupancy.getTypeHelper().getType() != null
							&& occupancy.getTypeHelper().getType().getCode().equals(U)) {
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_PP)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_12) <= 0;
							requiredBuildingHeight = "<= 12m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_PS)) {
							if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_DOB)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							}
							if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_DJ)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<=15m";
							}
							if (occupancy.getTypeHelper().getUsage().getCode().equals(U_PS_PTI)) {
								isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
								requiredBuildingHeight = "<= 15m";
							}

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_DMC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";

						}
						if (occupancy.getTypeHelper().getSubtype().getCode().equals(U_FP)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FS)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FTI)
								|| occupancy.getTypeHelper().getSubtype().getCode().equals(U_FTC)) {
							isAccepted = buildingHeight.compareTo(HEIGHT_AR_15) <= 0;
							requiredBuildingHeight = "<= 15m";
						}
					}
					LOG.info("isAccepted::" + isAccepted);
					if (errors.isEmpty() && StringUtils.isNotBlank(requiredBuildingHeight)) {
						Map<String, String> details = new HashMap<>();
						details.put(RULE_NO, ruleNo);
						details.put(DESCRIPTION, HEIGHT_BUILDING);
						// details.put(ZONE, " Zone " + ZONE_DC);
						details.put(PERMISSIBLE, requiredBuildingHeight);
						details.put(PROVIDED, String.valueOf(buildingHeight));
						details.put(STATUS,
								isAccepted ? Result.Accepted.getResultVal() : Result.Not_Accepted.getResultVal());

						scrutinyDetail.getDetail().add(details);
						pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
					}

				} else {
					errors.put(BUILDING_HEIGHT + block.getNumber(), getLocaleMessage(DcrConstants.OBJECTNOTDEFINED,
							BUILDING_HEIGHT + " for block " + block.getNumber()));
					pl.addErrors(errors);
				}
			}
		}

		return pl;
	}

	@Override
	public Plan process(Plan Plan) {
try {
	validate(Plan);
} catch (Exception e) {
	// TODO: handle exception
}
		
		/*
		 * scrutinyDetail = new ScrutinyDetail(); //
		 * scrutinyDetail.setKey("Common_Height of Building");
		 * scrutinyDetail.addColumnHeading(1, RULE_NO);
		 * scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		 * scrutinyDetail.addColumnHeading(3, ZONE); scrutinyDetail.addColumnHeading(4,
		 * PERMISSIBLE); scrutinyDetail.addColumnHeading(4, PROVIDED);
		 * scrutinyDetail.addColumnHeading(5, STATUS);
		 */
//		 if (!ProcessHelper.isSmallPlot(Plan)) {
//		 checkBuildingHeight(Plan);
//		 }
		// checkBuildingInSecurityZoneArea(Plan);

		return Plan;
	}

}
