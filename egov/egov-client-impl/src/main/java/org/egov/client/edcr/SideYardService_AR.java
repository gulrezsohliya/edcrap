package org.egov.client.edcr;

import static org.egov.client.constants.DxfFileConstants_AR.*;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.D;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.I;
import static org.egov.edcr.constants.DxfFileConstants.A_PO;
import static org.egov.edcr.utility.DcrConstants.FRONT_YARD_DESC;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD1_DESC;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD2_DESC;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD_DESC;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Building;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Plot;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.common.entity.edcr.SetBack;
import org.egov.common.entity.edcr.Yard;
import org.egov.edcr.constants.DxfFileConstants;
import org.egov.edcr.feature.SideYardService;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SideYardService_AR extends SideYardService {

	private static final BigDecimal SIDEVALUE_ONEPOINTFIVE = BigDecimal.valueOf(1.5);
	private static final BigDecimal SIDEVALUE_ONEPOINTEIGHT = BigDecimal.valueOf(1.8);
	private static final BigDecimal SIDEVALUE_TWO = BigDecimal.valueOf(2);
	private static final BigDecimal SIDEVALUE_TWOPOINTFIVE = BigDecimal.valueOf(2.5);
	private static final BigDecimal SIDEVALUE_THREE = BigDecimal.valueOf(3);
	private static final BigDecimal SIDEVALUE_THREEPOINTSIX = BigDecimal.valueOf(3.66);
	private static final BigDecimal SIDEVALUE_FOUR = BigDecimal.valueOf(4);
	private static final BigDecimal SIDEVALUE_FOURPOINTFIVE = BigDecimal.valueOf(4.5);
	private static final BigDecimal SIDEVALUE_FIVE = BigDecimal.valueOf(5);
	private static final BigDecimal SIDEVALUE_SIX = BigDecimal.valueOf(6);
	private static final BigDecimal SIDEVALUE_SEVEN = BigDecimal.valueOf(7);
	private static final BigDecimal SIDEVALUE_SEVENTYFIVE = BigDecimal.valueOf(0.75);
	private static final BigDecimal SIDEVALUE_EIGHT = BigDecimal.valueOf(8);
	private static final BigDecimal SIDEVALUE_NINE = BigDecimal.valueOf(9);
	private static final BigDecimal SIDEVALUE_TEN = BigDecimal.valueOf(10);

	// Arunachal side yard minimum
	private static final BigDecimal SIDEVALUE_ONE = BigDecimal.valueOf(1);
	private static final BigDecimal SIDEVALUE_ONE_TWO = BigDecimal.valueOf(1.2);
	
	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_450 = BigDecimal.valueOf(450);

	private static final String SIDENUMBER = "Side Number";
	private static final String MINIMUMLABEL = "Minimum distance ";

	private static final String RULE_35 = "35 Table-9";
	private static final String RULE_36 = "36";
	private static final String RULE_37_TWO_A = "37-2-A";
	private static final String RULE_37_TWO_B = "37-2-B";
	private static final String RULE_37_TWO_C = "37-2-C";
	private static final String RULE_37_TWO_D = "37-2-D";
	private static final String RULE_37_TWO_G = "37-2-G";
	private static final String RULE_37_TWO_H = "37-2-H";
	private static final String RULE_37_TWO_I = "37-2-I";
	private static final String RULE_47 = "47";
	private static final String SIDE_YARD_2_NOTDEFINED = "side2yardNodeDefined";
	private static final String SIDE_YARD_1_NOTDEFINED = "side1yardNodeDefined";

	public static final String BSMT_SIDE_YARD_DESC = "Basement Side Yard";
	private static final int PLOTAREA_300 = 300;
	public static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);

	private static final Logger LOG = Logger.getLogger(SideYardService_AR.class);

	private class SideYardResult {
		String rule;
		String subRule;
		String blockName;
		Integer level;
		BigDecimal actualMeanDistance = BigDecimal.ZERO;
		BigDecimal actualDistance = BigDecimal.ZERO;
		String occupancy;
		BigDecimal expectedDistance = BigDecimal.ZERO;
		BigDecimal expectedmeanDistance = BigDecimal.ZERO;
		boolean status = false;
	}

	public void processSideYard(final Plan pl) {
		HashMap<String, String> errors = new HashMap<>();
		Plot plot = null;
		if(pl.getPlot()!=null)
			plot = pl.getPlot();
		BigDecimal plotArea= null;
		if(pl.getPlot().getArea()!=null)
			plotArea = pl.getPlot().getArea();
		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;
		Boolean valid = false;

		validateSideYardRule(pl);

		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block
				OccupancyTypeHelper mostRestrictiveOccupancyType = block.getBuilding().getMostRestrictiveFarHelper();
				scrutinyDetail = new ScrutinyDetail();
				scrutinyDetail.addColumnHeading(1, RULE_NO);
				scrutinyDetail.addColumnHeading(2, LEVEL);
				scrutinyDetail.addColumnHeading(3, OCCUPANCY);
				scrutinyDetail.addColumnHeading(4, SIDENUMBER);
				scrutinyDetail.addColumnHeading(5, FIELDVERIFIED);
				scrutinyDetail.addColumnHeading(6, PERMISSIBLE);
				scrutinyDetail.addColumnHeading(7, PROVIDED);
				scrutinyDetail.addColumnHeading(8, STATUS);
				scrutinyDetail.setHeading(SIDE_YARD_DESC);
				SideYardResult sideYard1Result = new SideYardResult();
				SideYardResult sideYard2Result = new SideYardResult();

				for (SetBack setback : block.getSetBacks()) {
					Yard sideYard1 = null;
					Yard sideYard2 = null;

					if (setback.getSideYard1() != null
							&& setback.getSideYard1().getMinimumDistance().compareTo(BigDecimal.ZERO) > 0) {
						sideYard1 = setback.getSideYard1();
					}
					if (setback.getSideYard2() != null
							&& setback.getSideYard2().getMinimumDistance().compareTo(BigDecimal.ZERO) > 0) {
						sideYard2 = setback.getSideYard2();
					}
					if(setback.getLevel()>=0) {
						if (sideYard1 != null || sideYard2 != null) {
							double minlength = 0;
							double max = 0;
							if(sideYard1!=null && sideYard1.getMinimumDistance()!=null) {
								minlength=sideYard1.getMinimumDistance().doubleValue();
								max=sideYard1.getMinimumDistance().doubleValue();
							}
							else if(sideYard2!=null && sideYard2.getMinimumDistance()!=null) {
								minlength=sideYard2.getMinimumDistance().doubleValue();
								max=sideYard2.getMinimumDistance().doubleValue();
							}
								OCTYP_FOUND = false;
								OCTYP_NOTFOUND = false;
								Occupancy occupancy = new Occupancy();
								occupancy.setTypeHelper(block.getBuilding().getMostRestrictiveFarHelper());
									scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Side Setback");
									if (occupancy.getTypeHelper().getType() == null) {
										OCTYP_NOTFOUND = true; // occ typ not found
									} else {
										OCTYP_FOUND = true; // search for occ typ
										if ((mostRestrictiveOccupancyType.getType() != null
												&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) { // Residential

												sideYard1Result = checkSideYardForResidential_Side1(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
											
												sideYard2Result = checkSideYardForResidential_Side2(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
										}
										if (mostRestrictiveOccupancyType.getType() != null
												&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
												sideYard1Result = checkSideYardForCommercial_Side1(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
												sideYard2Result = checkSideYardForCommercial_Side2(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
										}
										if (mostRestrictiveOccupancyType.getType() != null
												&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

											
										}
										if (mostRestrictiveOccupancyType.getType() != null
												&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

										}
										if (mostRestrictiveOccupancyType.getType() != null
												&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

										}
										if (mostRestrictiveOccupancyType.getType() != null
												&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
												sideYard1Result = checkSideYardForPublicSemiPublic_Side1(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
												sideYard2Result = checkSideYardForPublicSemiPublic_Side2(pl, block.getBuilding(),
														 block.getName(), setback.getLevel(), plot,
														minlength, max,occupancy.getTypeHelper(), sideYard1Result, sideYard2Result,errors);
										}
								}

								if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
									errors.put("Block_" + block.getName() + "_" + SIDE_YARD_DESC, SIDE_YARD_DESC
											+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
									pl.addErrors(errors);
								}
								if (errors.isEmpty() && (pl.getErrors()==null)||pl.getErrors().isEmpty())
									addSideYardResult(pl, errors, sideYard1Result, sideYard2Result, sideYard1, sideYard2);
						}
					}
					

				}
			}
		}

	}

	private SideYardResult checkSideYardForCommercial_Side2(Plan pl, Building building, String blockName, Integer level,
			Plot plot, double minlength, double max, OccupancyTypeHelper mostRestrictiveOccupancyType, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result,HashMap<String, String> errors) {
		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal PlotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
				pl.addErrors(errors);
			}

		}
		if (max >= side2val.doubleValue())
			valid1 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(max), mostRestrictiveOccupancyType, sideYard2Result, valid1, subRule, rule,
				level);
	}

	private SideYardResult checkSideYardForCommercial_Side1(Plan pl, Building building, String blockName, Integer level,
			Plot plot, double minlength, double max, OccupancyTypeHelper mostRestrictiveOccupancyType, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result,HashMap<String, String> errors) {
		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal PlotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
				pl.addErrors(errors);
			}

		}
		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard2Result(blockName, side1val, BigDecimal.valueOf(max), mostRestrictiveOccupancyType, sideYard2Result, valid1, subRule, rule,
				level);
	}

	private SideYardResult checkSideYardForPublicSemiPublic_Side2(Plan pl, Building building, String blockName,
			Integer level, Plot plot, double minlength, double max, OccupancyTypeHelper mostRestrictiveOccupancyType,
			SideYardResult sideYard1Result, SideYardResult sideYard2Result,HashMap<String, String> errors) {
		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal PlotArea = pl.getPlot().getArea();

		//Hostel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
			if(PlotArea.compareTo(PLOT_AREA_100)<0) {
				errors.put("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 hostel","Plot area should not exceed above 3000 sqmts for hostel");
				pl.addErrors(errors);
			}

		}
		if (max >= side2val.doubleValue())
			valid1 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(max), mostRestrictiveOccupancyType, sideYard2Result, valid1, subRule, rule,
				level);
	}

	private SideYardResult checkSideYardForPublicSemiPublic_Side1(Plan pl, Building building, String blockName,
			Integer level, Plot plot, double minlength, double max, OccupancyTypeHelper mostRestrictiveOccupancyType,
			SideYardResult sideYard1Result, SideYardResult sideYard2Result,HashMap<String, String> errors) {
		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal PlotArea = pl.getPlot().getArea();

		//Hostel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
			if(PlotArea.compareTo(PLOT_AREA_100)<0) {
				errors.put("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 hostel","Plot area should not exceed above 3000 sqmts for hostel");
				pl.addErrors(errors);
			}

		}
		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard1Result(blockName, side1val, BigDecimal.valueOf(max), mostRestrictiveOccupancyType, sideYard1Result, valid1, subRule, rule,
				level);
	}

	private void addSideYardResult(final Plan pl, HashMap<String, String> errors, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result, Yard sideYard1, Yard sideYard2) {

		if (errors.isEmpty()) {

			if (sideYard1Result != null) {
				Map<String, String> details = new HashMap<>();
				details.put(RULE_NO, sideYard1Result.subRule);
				details.put(LEVEL, sideYard1Result.level != null ? sideYard1Result.level.toString() : "");
				details.put(OCCUPANCY, sideYard1Result.occupancy);

				details.put(FIELDVERIFIED, MINIMUMLABEL);
				details.put(PERMISSIBLE, sideYard1Result.expectedDistance.toString());
				details.put(PROVIDED, sideYard1Result.actualDistance.toString());

				details.put(SIDENUMBER, SIDE_YARD1_DESC);

				if (sideYard1Result.status) {
					details.put(STATUS, Result.Accepted.getResultVal());
				} else {
					details.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(details);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}

			if (sideYard2Result != null) {
				Map<String, String> detailsSideYard2 = new HashMap<>();
				detailsSideYard2.put(RULE_NO, sideYard2Result.subRule);
				detailsSideYard2.put(LEVEL, sideYard2Result.level != null ? sideYard2Result.level.toString() : "");
				detailsSideYard2.put(OCCUPANCY, sideYard2Result.occupancy);
				detailsSideYard2.put(SIDENUMBER, SIDE_YARD2_DESC);

				detailsSideYard2.put(FIELDVERIFIED, MINIMUMLABEL);
				detailsSideYard2.put(PERMISSIBLE, sideYard2Result.expectedDistance.toString());
				detailsSideYard2.put(PROVIDED, sideYard2Result.actualDistance.toString());
				// }
				if (sideYard2Result.status) {
					detailsSideYard2.put(STATUS, Result.Accepted.getResultVal());
				} else {
					detailsSideYard2.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(detailsSideYard2);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}
		}
	}

	private SideYardResult checkSideYardForResidential_Side1(final Plan pl, Building building,
			 String blockName, Integer level, final Plot plot, final double min,
			final double max,final OccupancyTypeHelper mostRestrictiveOccupancyType, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result,HashMap<String, String> errors) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal PlotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)){
			side1val=SIDEVALUE_ONE;
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 Residential","Plot area below 48 sqmts not allowed for plotted residential housing");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
				side1val=SIDEVALUE_ONE;
			} else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				side1val=SIDEVALUE_ONE;

			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side1val=SIDEVALUE_ONE_TWO;
			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				errors.put("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
				pl.addErrors(errors);
			}
		}
		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard1Result(blockName, side1val, BigDecimal.valueOf(max), mostRestrictiveOccupancyType, sideYard1Result, valid1, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForResidential_Side2(final Plan pl, Building building,
			 String blockName, Integer level, final Plot plot, final double min,
			final double max,final OccupancyTypeHelper mostRestrictiveOccupancyType, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result,HashMap<String, String> errors) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;
		Boolean valid2 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal PlotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)){
			side2val=SIDEVALUE_ONE;
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 Residential","Plot area below 48 sqmts not allowed for plotted residential housing");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
				side2val=SIDEVALUE_ONE;
			} else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				side2val=SIDEVALUE_ONE;

			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				side2val=SIDEVALUE_ONE_TWO;
			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				errors.put("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
				pl.addErrors(errors);
			}
		}

		if (min >= side2val.doubleValue())
			valid2 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(min), mostRestrictiveOccupancyType, sideYard2Result, valid2, subRule, rule,
				level);

	}


	private SideYardResult compareSideYard1Result(String blockName, BigDecimal exptDistance, BigDecimal actualDistance,
			OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result, Boolean valid, String subRule,
			String rule, Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else if (mostRestrictiveOccupancy.getType() != null)
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		else
			occupancyName = "";

		if (exptDistance.compareTo(sideYard1Result.expectedDistance) >= 0) {
			if (exptDistance.compareTo(sideYard1Result.expectedDistance) == 0) {
				sideYard1Result.rule = sideYard1Result.rule != null ? sideYard1Result.rule + "," + rule : rule;
				sideYard1Result.occupancy = sideYard1Result.occupancy != null
						? sideYard1Result.occupancy + "," + occupancyName
						: occupancyName;
			} else {
				sideYard1Result.rule = rule;
				sideYard1Result.occupancy = occupancyName;
			}

			sideYard1Result.subRule = subRule;
			sideYard1Result.blockName = blockName;
			sideYard1Result.level = level;
			sideYard1Result.actualDistance = actualDistance;
			sideYard1Result.expectedDistance = exptDistance;
			sideYard1Result.status = valid;
		}
		return sideYard1Result;
	}

	private SideYardResult compareSideYard2Result(String blockName, BigDecimal exptDistance, BigDecimal actualDistance,
			OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard2Result, Boolean valid, String subRule,
			String rule, Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else if (mostRestrictiveOccupancy.getType() != null)
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		else
			occupancyName = "";

		if (exptDistance.compareTo(sideYard2Result.expectedDistance) >= 0) {
			if (exptDistance.compareTo(sideYard2Result.expectedDistance) == 0) {

				sideYard2Result.rule = sideYard2Result.rule != null ? sideYard2Result.rule + "," + rule : rule;
				sideYard2Result.occupancy = sideYard2Result.occupancy != null
						? sideYard2Result.occupancy + "," + occupancyName
						: occupancyName;

			} else {
				sideYard2Result.rule = rule;
				sideYard2Result.occupancy = occupancyName;
			}

			sideYard2Result.subRule = subRule;
			sideYard2Result.blockName = blockName;
			sideYard2Result.level = level;
			sideYard2Result.actualDistance = actualDistance;
			sideYard2Result.expectedDistance = exptDistance;
			sideYard2Result.status = valid;
		}

		return sideYard2Result;
	}

	private void validateSideYardRule(final Plan pl) {

		for (Block block : pl.getBlocks()) {
			if (!block.getCompletelyExisting()) {
				Boolean sideYardDefined = false;
				for (SetBack setback : block.getSetBacks()) {
					if (setback.getSideYard1() != null
							&& setback.getSideYard1().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
						sideYardDefined = true;
					} else if (setback.getSideYard2() != null
							&& setback.getSideYard2().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
						sideYardDefined = true;
					}
				}
//				if (!sideYardDefined) {
//					HashMap<String, String> errors = new HashMap<>();
//					errors.put(SIDE_YARD_DESC,
//							prepareMessage(OBJECTNOTDEFINED, SIDE_YARD_DESC + " for Block " + block.getName()));
//					pl.addErrors(errors);
//				}
			}

		}

	}

}
