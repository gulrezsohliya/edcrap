package org.egov.client.edcr;

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
import org.springframework.stereotype.Service;

@Service
public class BuildingHeight_AR extends BuildingHeight {
  private static final Logger LOG = Logger.getLogger(BuildingHeight_AR.class);
  
  public static final BigDecimal HEIGHT_AR_04_5 = BigDecimal.valueOf(4.5D);
  
  public static final BigDecimal HEIGHT_AR_06 = BigDecimal.valueOf(6L);
  
  public static final BigDecimal HEIGHT_AR_09 = BigDecimal.valueOf(9L);
  
  public static final BigDecimal HEIGHT_AR_12 = BigDecimal.valueOf(12L);
  
  public static final BigDecimal HEIGHT_AR_14 = BigDecimal.valueOf(14L);
  
  public static final BigDecimal HEIGHT_AR_15 = BigDecimal.valueOf(15L);
  
  public static final BigDecimal HEIGHT_AR_18 = BigDecimal.valueOf(18L);
  
  public static final BigDecimal HEIGHT_AR_20 = BigDecimal.valueOf(20L);
  
  public static final BigDecimal HEIGHT_AR_25 = BigDecimal.valueOf(25L);
  
  public static final BigDecimal HEIGHT_AR_26 = BigDecimal.valueOf(26L);
  
  public static final BigDecimal HEIGHT_AR_30 = BigDecimal.valueOf(30L);
  
  public static final BigDecimal HEIGHT_AR_37 = BigDecimal.valueOf(37L);
  
  public static final BigDecimal HEIGHT_AR_50 = BigDecimal.valueOf(50L);
  
  public static final BigDecimal HEIGHT_AR_8POINT4 = BigDecimal.valueOf(8.4D);
  
  public static final BigDecimal HEIGHT_AR_14POINT4 = BigDecimal.valueOf(14.4D);
  
  public static final BigDecimal HEIGHT_AR_17POINT4 = BigDecimal.valueOf(17.4D);
  
  private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48L);
  
  private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60L);
  
  private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100L);
  
  private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250L);
  
  private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500L);
  
  private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000L);
  
  private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500L);
  
  private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000L);
  
  private static final BigDecimal PLOT_AREA_5000 = BigDecimal.valueOf(5000L);
  
  private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000L);
  
  private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000L);
  
  private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000L);
  
  private static final BigDecimal PLOT_AREA_28000 = BigDecimal.valueOf(28000L);
  
  private static final BigDecimal PLOT_AREA_6000 = BigDecimal.valueOf(6000L);
  
  private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000L);
  
  private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000L);
  
  private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(1080L);
  
  private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(510L);
  
  private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400L);
  
  private static final BigDecimal PLOT_WIDTH_8 = BigDecimal.valueOf(8L);
  
  private static final String HEIGHT_BUILDING = "Maximum height of building allowed...";
  
  private static final String ZONE = "Zone";
  
  private static final String RULE_TBD = "Zone";
  
  private static final String BUILDING_HEIGHT = "Building Height";
  
  private static final BigDecimal HEIGHT_AR_8 = BigDecimal.valueOf(8L);
  
  private static final BigDecimal PLOT_AREA_450 = BigDecimal.valueOf(50L);
  
  private static final BigDecimal HEIGHT_AR_9 = BigDecimal.valueOf(9L);
  
  private static final BigDecimal HEIGHT_AR_6 = BigDecimal.valueOf(6L);
  
  public Plan validate(Plan pl) {
    String OccupancyType = pl.getPlanInformation().getLandUseZone().toUpperCase();
    OccupancyTypeHelper mostRestrictiveOccupancyType = Util_AR.getMostRestrictive(pl);
    this.scrutinyDetail = new ScrutinyDetail();
    this.scrutinyDetail.addColumnHeading(Integer.valueOf(1), "Byelaw");
    this.scrutinyDetail.addColumnHeading(Integer.valueOf(2), "Description");
    this.scrutinyDetail.addColumnHeading(Integer.valueOf(5), "Permissible");
    this.scrutinyDetail.addColumnHeading(Integer.valueOf(6), "Provided");
    this.scrutinyDetail.addColumnHeading(Integer.valueOf(7), "Status");
    HashMap<String, String> errors = new HashMap<>();
    BigDecimal PlotArea = pl.getPlanInformation().getPlotArea();
    BigDecimal PlotWidth = pl.getPlanInformation().getWidthOfPlot();
    for (Block block : pl.getBlocks()) {
      List<OccupancyTypeHelper> plotWiseOccupancyTypes = new ArrayList<>();
      for (Occupancy occupancy : block.getBuilding().getOccupancies()) {
        if (occupancy.getTypeHelper().getType() != null) {
          String ruleNo = "Zone";
          this.scrutinyDetail.setKey("Block_" + block.getNumber() + "_" + "Height of Building");
          if (block.getBuilding() != null && (block.getBuilding().getBuildingHeight() != null || 
            block.getBuilding().getBuildingHeight().compareTo(BigDecimal.ZERO) > 0)) {
            BigDecimal maxBuildingHeight = BigDecimal.ZERO;
            BigDecimal buildingHeight = block.getBuilding().getBuildingHeight();
            LOG.info("buildingHeight w/o stilt parking:" + buildingHeight);
            BigDecimal ParapetHeight = BigDecimal.ZERO;
            BigDecimal stiltHeight = BigDecimal.ZERO;
            for (Floor floor : block.getBuilding().getFloors()) {
              if (floor.getParking().getStilts() != null && !floor.getParking().getStilts().isEmpty())
                stiltHeight = stiltHeight.add(floor.getParking().getStilts().stream()
                    .map(Measurement::getHeight).reduce(BigDecimal.ZERO, BigDecimal::add)); 
            } 
            LOG.info("Stilt Height:" + stiltHeight);
            buildingHeight = buildingHeight.add(stiltHeight);
            LOG.info("buildingHeight w stilt :" + buildingHeight);
            if (mostRestrictiveOccupancyType != null) {
              if (mostRestrictiveOccupancyType.getType() != null && 
                "R".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightResidential(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
              if (mostRestrictiveOccupancyType.getType() != null && 
                "C".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightCommercial(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
              if (mostRestrictiveOccupancyType.getType() != null && 
                "I".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightIndustrial(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
              if (mostRestrictiveOccupancyType.getType() != null && 
                "G".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightGovernmentUse(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
              if (mostRestrictiveOccupancyType.getType() != null && 
                "T".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightTransportation(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
              if (mostRestrictiveOccupancyType.getType() != null && 
                "P".equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))
                processBuildingHeightPublicSemiPublic(pl, mostRestrictiveOccupancyType, PlotArea, stiltHeight, buildingHeight, maxBuildingHeight); 
            } 
          } 
        } 
      } 
    } 
    return pl;
  }
  
  private void processBuildingHeightIndustrial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("I1a"))
      if (PlotArea.compareTo(PLOT_AREA_2000) < 0) {
        pl.addError("PlotArea below 2000 Flatted", "Plot area below 2000 sqmts not allowed for Flatted");
      } else if (PlotArea.compareTo(PLOT_AREA_2000) >= 0) {
        maxBuildingHeight = HEIGHT_AR_15;
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("I1b"))
      if (((String)pl.getPlanInfoProperties().get("TERRAIN")).equals("PLAINS")) {
        if (PlotArea.compareTo(PLOT_AREA_400) <= 0)
          maxBuildingHeight = HEIGHT_AR_12; 
        if (PlotArea.compareTo(PLOT_AREA_400) > 0 && PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } else if (PlotArea.compareTo(PLOT_AREA_4000) > 0 && PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (((String)pl.getPlanInfoProperties().get("TERRAIN")).equals("HILLS")) {
        if (PlotArea.compareTo(PLOT_AREA_400) <= 0)
          maxBuildingHeight = HEIGHT_AR_9; 
        if (PlotArea.compareTo(PLOT_AREA_400) > 0 && PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } else if (PlotArea.compareTo(PLOT_AREA_4000) > 0 && PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else {
        pl.addError("TERRAIN TYPE", "Terrain Type should be PLAINS/HILLS");
      }  
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void processBuildingHeightCommercial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C1a") || 
      mostRestrictiveOccupancyType.getSubtype().getCode().equals("C1b"))
      if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_8POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_06;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_17POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_15;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_17POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_15;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_17POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_15;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_17POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_15;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
        pl.addError("PlotArea above 3000 Restaurant", "Plot area above 3000 sqmts not allowed for restaurants");
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C1c"))
      if (PlotArea.compareTo(PLOT_AREA_450) < 0) {
        pl.addError("PlotArea below 450 Shopping", "Plot area below 450 sqmts not allowed for Shopping");
      } else if (PlotArea.compareTo(PLOT_AREA_450) >= 0) {
        maxBuildingHeight = HEIGHT_AR_18;
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C2a"))
      if (PlotArea.compareTo(PLOT_AREA_250) < 0) {
        pl.addError("PlotArea below 250 Hotel", "Plot area below 250 sqmts not allowed for hotel");
      } else if (PlotArea.compareTo(PLOT_AREA_250) >= 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
        maxBuildingHeight = HEIGHT_AR_15;
      } else {
        if (PlotArea.compareTo(PLOT_AREA_500) >= 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0)
          return; 
        if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0)
          return; 
        if (PlotArea.compareTo(PLOT_AREA_1500) > 0)
          return; 
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C3a"))
      return; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C4a"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("C5a"))
      return; 
    if (mostRestrictiveOccupancyType.getUsage().getCode().equals("C6a"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void processBuildingHeightPublicSemiPublic(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P3a") || 
      mostRestrictiveOccupancyType.getSubtype().getCode().equals("P3b"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P3c"))
      if (PlotArea.compareTo(PLOT_AREA_100) < 0) {
        pl.addError("PlotArea below 100 Hostel", "Plot area below 100 sqmts not allowed for hostel");
      } else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
        maxBuildingHeight = HEIGHT_AR_14POINT4;
      } else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
        maxBuildingHeight = HEIGHT_AR_17POINT4;
      } else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
        maxBuildingHeight = HEIGHT_AR_17POINT4;
      } else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
        maxBuildingHeight = HEIGHT_AR_17POINT4;
      } else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
        maxBuildingHeight = HEIGHT_AR_17POINT4;
      } else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
        pl.addError("PlotArea above 3000 Hostel", "Plot area above 3000 sqmts not allowed for hostel");
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P2a"))
      return; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P2b"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P2c"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P2d"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1b"))
      maxBuildingHeight = HEIGHT_AR_6; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1c"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1d"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1e"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1g"))
      maxBuildingHeight = HEIGHT_AR_9; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1f"))
      return; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("P1a"))
      return; 
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void processBuildingHeightTransportation(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal plotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("I1b"))
      return; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("I1a") || 
      mostRestrictiveOccupancyType.getSubtype().getCode().equals("I1c"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void processBuildingHeightGovernmentUse(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal plotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("G2a"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("G1a"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("G3a"))
      maxBuildingHeight = HEIGHT_AR_12; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("G3b"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("G3c"))
      maxBuildingHeight = HEIGHT_AR_15; 
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void processBuildingHeightResidential(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea, BigDecimal stiltHeight, BigDecimal buildingHeight, BigDecimal maxBuildingHeight) {
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("R1a"))
      if (pl.getPlot().getWidth().compareTo(HEIGHT_AR_8) <= 0) {
        maxBuildingHeight = HEIGHT_AR_09;
      } else {
        pl.addError("Building Height ", " Width of plot is greater than 8m");
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("R1b") || 
      mostRestrictiveOccupancyType.getSubtype().getCode().equals("R1c"))
      if (PlotArea.compareTo(PLOT_AREA_48) < 0) {
        pl.addError("PlotArea below 48 Residential", "Plot area below 48 sqmts not allowed for plotted residential housing");
      } else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_8POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_06;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_17POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_15;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
        if (stiltHeight != BigDecimal.ZERO && stiltHeight != null) {
          maxBuildingHeight = HEIGHT_AR_14POINT4;
        } else {
          maxBuildingHeight = HEIGHT_AR_12;
        } 
      } else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
        pl.addError("PlotArea above 3000 Residential", "Plot area exceeding 3000 sqmts not allowed for plotted residential housing");
      }  
    if (mostRestrictiveOccupancyType.getSubtype().getCode().equals("R2a"))
      if (PlotArea.compareTo(PLOT_AREA_10000) < 0) {
        pl.addError("PlotArea below 10000 R2a", "Plot area below 10000 sqmts not allowed for farm house");
      } else if (PlotArea.compareTo(PLOT_AREA_10000) >= 0 && PlotArea.compareTo(PLOT_AREA_20000) < 0) {
        maxBuildingHeight = HEIGHT_AR_06;
      } else if (PlotArea.compareTo(PLOT_AREA_20000) >= 0) {
        maxBuildingHeight = HEIGHT_AR_06;
      }  
    if (maxBuildingHeight.compareTo(BigDecimal.ZERO) > 0)
      buildResult(pl, this.scrutinyDetail, buildingHeight, maxBuildingHeight, "Zone"); 
  }
  
  private void buildResult(Plan pl, ScrutinyDetail scrutinyDetail, BigDecimal buildingHeight, BigDecimal maxBuildingHeight, String ruleNo) {
    boolean isAccepted = false;
    if (buildingHeight.compareTo(maxBuildingHeight) <= 0)
      isAccepted = true; 
    Map<String, String> details = new HashMap<>();
    details.put("Byelaw", ruleNo);
    details.put("Description", "Maximum height of building allowed...");
    details.put("Permissible", maxBuildingHeight.toString().concat(" m"));
    details.put("Provided", String.valueOf(buildingHeight).concat(" m"));
    details.put("Status", isAccepted ? Result.Accepted.getResultVal() : 
        Result.Not_Accepted.getResultVal());
    scrutinyDetail.getDetail().add(details);
    pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
  }
  
  public Plan process(Plan plan) {
    try {
      validate(plan);
    } catch (Exception exception) {}
    return plan;
  }
}
