package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public abstract class RecordValidator implements Validator {

  // fields that require ontology terms
  @Nonnull protected static final String CHEMICAL_ADMINISTRATION_ATTR_NAME = "chem_administration";
  @Nonnull protected static final String DISEASE_ATTR_NAME = "disease";
  @Nonnull protected static final String ENVIRONMENT_BIOME_ATTR_NAME = "env_biome";
  @Nonnull protected static final String ENVIRONMENT_FEATURE_ATTR_NAME = "env_feature";
  @Nonnull protected static final String ENVIRONMENT_MATERIAL_ATTR_NAME = "env_material";
  @Nonnull protected static final String HOST_DISEASE_ATTR_NAME = "host_disease";
  @Nonnull protected static final String HOST_TISSUE_SAMPLED_ATTR_NAME = "host_tissue_sampled";
  @Nonnull protected static final String PHENOTYPE_ATTR_NAME = "phenotype";
  @Nonnull protected static final String PLANT_BODY_SITE_ATTR_NAME = "plant_body_site";

  // fields that require a return type of "{term}"
  @Nonnull protected static final String BODY_HABITAT_ATTR_NAME = "body_habitat";
  @Nonnull protected static final String GEO_LOCATION_ATTR_NAME = "geo_loc_name";
  @Nonnull protected static final String HEALTH_STATE_ATTR_NAME = "health_state";
  @Nonnull protected static final String HOST_BODY_HABITAT_ATTR_NAME = "host_body_habitat";
  @Nonnull protected static final String PATHOGENICITY_ATTR_NAME = "pathogenicity";
  @Nonnull protected static final String PLOIDY_ATTR_NAME = "ploidy";
  @Nonnull protected static final String PROPAGATION_ATTR_NAME = "propagation";

  // fields that require a return type of "{boolean}" alone (i.e., not combined with some other return type)
  @Nonnull protected static final String HYSTERECTOMY_ATTR_NAME = "hysterectomy";
  @Nonnull protected static final String MEDICAL_HISTORY_PERFORMED_ATTR_NAME = "medic_hist_perform";
  @Nonnull protected static final String SMOKER_ATTR_NAME = "smoker";
  @Nonnull protected static final String TWIN_SIBLING_ATTR_NAME = "twin_sibling";

  // fields that require a return type of "{integer}" alone (i.e., not combined with some other return type)
  @Nonnull protected static final String EXTRACHROMOSOMAL_ELEMENTS_ATTR_NAME = "extrachrom_elements";
  @Nonnull protected static final String HOST_TAXONOMY_ID_ATTR_NAME = "host_taxid";
  @Nonnull protected static final String MEDICATION_CODE_ATTR_NAME = "ihmc_medication_code";
  @Nonnull protected static final String NR_REPLICONS_ATTR_NAME = "num_replicons";
  @Nonnull protected static final String OCCUPANCY_AT_SAMPLING_ATTR_NAME = "occup_samp";

  // fields that require a return value that comes from a pre-defined value set
  @Nonnull protected static final String EDTA_INHIBITOR_ATTR_NAME = "edta_inhibitor_tested";
  @Nonnull protected static final String BETA_LACTAMASE_ATTR_NAME = "beta_lactamase_family";
  @Nonnull protected static final String DOMINANT_HAND_ATTR_NAME = "dominant_hand";
  @Nonnull protected static final String DRAINAGE_CLASS_ATTR_NAME = "drainage_class";
  @Nonnull protected static final String FILTER_TYPE_ATTR_NAME = "filter_type";
  @Nonnull protected static final String HEATING_COOLING_TYPE_ATTR_NAME = "heat_cool_type";
  @Nonnull protected static final String HORIZON_ATTR_NAME = "horizon";
  @Nonnull protected static final String HOST_SEX_ATTR_NAME = "host_sex";
  @Nonnull protected static final String INDOOR_SPACE_ATTR_NAME = "indoor_space";
  @Nonnull protected static final String INDOOR_SURFACE_ATTR_NAME = "indoor_surf";
  @Nonnull protected static final String INVESTIGATION_TYPE_ATTR_NAME = "investigation_type";
  @Nonnull protected static final String LIGHT_TYPE_ATTR_NAME = "light_type";
  @Nonnull protected static final String BIOTIC_RELATIONSHIP_ATTR_NAME = "biotic_relationship";
  @Nonnull protected static final String OXYGENATION_STATUS_ATTR_NAME = "oxy_stat_samp";
  @Nonnull protected static final String PROFILE_POSITION_ATTR_NAME = "profile_position";
  @Nonnull protected static final String RELATIONSHIP_TO_OXYGEN_ATTR_NAME = "rel_to_oxygen";
  @Nonnull protected static final String SEDIMENT_TYPE_ATTR_NAME = "sediment_type";
  @Nonnull protected static final String SEX_ATTR_NAME = "sex";
  @Nonnull protected static final String SPACE_TYPICAL_STATE_ATTR_NAME = "space_typ_state";
  @Nonnull protected static final String SUBSTRUCTURE_TYPE_ATTR_NAME = "substructure_type";
  @Nonnull protected static final String SURFACE_MATERIAL_ATTR_NAME = "surf_material";
  @Nonnull protected static final String TIDAL_STAGE_ATTR_NAME = "tidal_stage";
  @Nonnull protected static final String TROPHIC_LEVEL_ATTR_NAME = "trophic_level";
  @Nonnull protected static final String URINE_COLLECTION_ATTR_NAME = "urine_collect_meth";
  @Nonnull protected static final String TILLAGE_ATTR_NAME = "tillage";
  @Nonnull protected static final String SUPER_POPULATION_ATTR_NAME = "super_population_code";
  @Nonnull protected static final String SUPER_POPULATION_DESCRIPTION_ATTR_NAME = "super_population_description";
  @Nonnull protected static final String BUILDING_OCCUPANCY_TYPE_ATTR_NAME = "build_occup_type";
  @Nonnull protected static final String BUILDING_SETTING_ATTR_NAME = "building_setting";
  @Nonnull protected static final String CARBAPENEMASE_ATTR_NAME = "carbapenemase";
  @Nonnull protected static final String ENVIRONMENTAL_PACKAGE_ATTR_NAME = "env_package";
  @Nonnull protected static final String SURFACE_AIR_CONTAMINANT_ATTR_NAME = "surf_air_cont";


  // value sets for attributes
  @Nonnull protected static final String[] EDTA_INHIBITOR_VALUE_SET = {"yes", "no"};
  @Nonnull protected static final String[] DOMINANT_HAND_VALUE_SET = {"left", "right", "ambidextrous"};
  @Nonnull protected static final String[] LIGHT_TYPE_VALUE_SET = {"natural light", "electric light", "no light"};
  @Nonnull protected static final String[] BIOTIC_RELATIONSHIP_VALUE_SET = {"free living", "parasite", "commensal", "symbiont"};
  @Nonnull protected static final String[] OXYGENATION_STATUS_VALUE_SET = {"aerobic", "anaerobic"};
  @Nonnull protected static final String[] PROFILE_POSITION_VALUE_SET = {"summit", "shoulder", "backslope", "footslope", "toeslope"};
  @Nonnull protected static final String[] SEDIMENT_TYPE_VALUE_SET = {"biogenous", "cosmogenous", "hydrogenous", "lithogenous"};
  @Nonnull protected static final String[] SPACE_TYPICAL_STATE_VALUE_SET = {"typical occupied", "typically unoccupied"};
  @Nonnull protected static final String[] SUBSTRUCTURE_TYPE_VALUE_SET = {"crawlspace", "slab on grade", "basement"};
  @Nonnull protected static final String[] TIDAL_STAGE_VALUE_SET = {"low", "high"};
  @Nonnull protected static final String[] URINE_COLLECTION_VALUE_SET = {"clean catch", "catheter"};
  @Nonnull protected static final String[] BUILDING_SETTING_VALUE_SET = {"urban", "suburban", "exurban", "rural"};
  @Nonnull protected static final String[] CARBAPENEMASE_VALUE_SET = {"yes", "no"};
  @Nonnull protected static final String[] SUPER_POPULATION_VALUE_SET = {"AFR", "AMR", "EAS", "EUR", "SAS"};
  @Nonnull protected static final String[] SUPER_POPULATION_DESCRIPTION_VALUE_SET = {"African", "Ad Mixed American",
      "East Asian", "European", "South Asian"};
  @Nonnull protected static final String[] DRAINAGE_CLASS_VALUE_SET = {"very poorly", "poorly", "somewhat poorly",
      "moderately well", "well", "excessively drained"};
  @Nonnull protected static final String[] FILTER_TYPE_VALUE_SET = {"particulate air filter", "chemical air filter",
      "low-MERV pleated media", "HEPA", "electrostatic", "gas-phase or ultraviolet air treatments"};
  @Nonnull protected static final String[] HEATING_COOLING_TYPE_VALUE_SET = {"radiant system", "heat pump", "forced air system",
      "steam forced heat", "wood stove"};
  @Nonnull protected static final String[] HORIZON_VALUE_SET = {"O horizon", "A horizon", "E horizon", "B horizon",
      "C horizon", "R layer", "Permafrost"};
  @Nonnull protected static final String[] HOST_SEX_VALUE_SET = {"male", "female", "pooled male and female",
      "neuter", "hermaphrodite", "intersex", "not determined"};
  @Nonnull protected static final String[] INDOOR_SPACE_VALUE_SET = {"bedroom", "office", "bathroom", "foyer",
      "kitchen", "locker room", "hallway", "elevator"};
  @Nonnull protected static final String[] INDOOR_SURFACE_VALUE_SET = {"counter top", "window", "wall", "cabinet",
      "ceiling", "door", "shelving", "vent cover"};
  @Nonnull protected static final String[] INVESTIGATION_TYPE_VALUE_SET = {"eukaryote", "bacteria_archaea", "plasmid",
      "virus", "organelle", "metagenome", "miens-survey", "miens-culture"};
  @Nonnull protected static final String[] SURFACE_MATERIAL_VALUE_SET = {"concrete", "wood", "stone", "tile", "plastic",
      "glass", "vinyl", "metal", "carpet", "stainless steel", "paint", "cinder blocks", "hay bales", "stucco", "adobe"};
  @Nonnull protected static final String[] RELATIONSHIP_TO_OXYGEN_VALUE_SET = {"aerobe", "anaerobe", "facultative",
      "microaerophilic", "microanaerobe", "obligate aerobe", "obligate anaerobe"};
  @Nonnull protected static final String[] TILLAGE_VALUE_SET = {"drill", "cutting disc", "ridge till", "strip tillage",
      "zonal tillage", "chisel", "tined", "mouldboard", "disc plough"};
  @Nonnull protected static final String[] BUILDING_OCCUPANCY_TYPE_VALUE_SET = {"office", "market", "restaurant", "residence",
      "school", "residential", "commercial", "low rise", "high rise", "wood framed", "health care", "airport", "sports complex"};
  @Nonnull protected static final String[] SURFACE_AIR_CONTAMINANT_VALUE_SET = {"dust", "organic matter", "particulate matter",
      "volatile organic compounds", "biological contaminants", "radon", "nutrients", "biocides"};
  @Nonnull protected static final String[] BETA_LACTAMASE_VALUE_SET = {"ACC", "ACT", "ADC", "BEL", "CARB", "CBP", "CFE",
      "CMY", "CTX-M", "DHA", "FOX", "GES", "GIM", "KPC", "IMI", "IMP", "IND", "LAT", "MIR", "MOX", "NDM", "OXA",
      "PER", "PDC", "SHV", "SME", "TEM", "VEB", "VIM"};
  @Nonnull protected static final String[] ENVIRONMENTAL_PACKAGE_VALUE_SET = {"air", "host-associated", "human-associated",
      "human-skin", "human-oral", "human-gut", "human-vaginal", "microbial", "miscellaneous", "plant-associated", "sediment",
      "soil", "wastewater", "water"};
  @Nonnull protected static final String[] TROPHIC_LEVEL_VALUE_SET = {"autotroph", "carboxydotroph", "chemoautotroph",
      "chemoheterotroph", "chemolithoautotroph", "chemolithotroph", "chemoorganoheterotroph", "chemoorganotroph",
      "chemosynthetic", "chemotroph", "copiotroph", "diazotroph", "facultative", "heterotroph", "lithoautotroph",
      "lithoheterotroph", "lithotroph", "methanotroph", "methylotroph", "mixotroph", "obligate", "chemoautolithotroph",
      "oligotroph", "organoheterotroph", "organotroph", "photoautotroph", "photoheterotroph", "photolithoautotroph",
      "photolithotroph", "photosynthetic", "phototroph"};


  @Nonnull protected static Map<String,List<String>> VALUE_SETS = new HashMap<>();
  static {
    VALUE_SETS.put(EDTA_INHIBITOR_ATTR_NAME, Arrays.asList(EDTA_INHIBITOR_VALUE_SET));
    VALUE_SETS.put(BETA_LACTAMASE_ATTR_NAME, Arrays.asList(BETA_LACTAMASE_VALUE_SET));
    VALUE_SETS.put(DOMINANT_HAND_ATTR_NAME, Arrays.asList(DOMINANT_HAND_VALUE_SET));
    VALUE_SETS.put(DRAINAGE_CLASS_ATTR_NAME, Arrays.asList(DRAINAGE_CLASS_VALUE_SET));
    VALUE_SETS.put(FILTER_TYPE_ATTR_NAME, Arrays.asList(FILTER_TYPE_VALUE_SET));
    VALUE_SETS.put(HEATING_COOLING_TYPE_ATTR_NAME, Arrays.asList(HEATING_COOLING_TYPE_VALUE_SET));
    VALUE_SETS.put(HORIZON_ATTR_NAME, Arrays.asList(HORIZON_VALUE_SET));
    VALUE_SETS.put(HOST_SEX_ATTR_NAME, Arrays.asList(HOST_SEX_VALUE_SET));
    VALUE_SETS.put(INDOOR_SPACE_ATTR_NAME, Arrays.asList(INDOOR_SPACE_VALUE_SET));
    VALUE_SETS.put(INDOOR_SURFACE_ATTR_NAME, Arrays.asList(INDOOR_SURFACE_VALUE_SET));
    VALUE_SETS.put(INVESTIGATION_TYPE_ATTR_NAME, Arrays.asList(INVESTIGATION_TYPE_VALUE_SET));
    VALUE_SETS.put(LIGHT_TYPE_ATTR_NAME, Arrays.asList(LIGHT_TYPE_VALUE_SET));
    VALUE_SETS.put(BIOTIC_RELATIONSHIP_ATTR_NAME, Arrays.asList(BIOTIC_RELATIONSHIP_VALUE_SET));
    VALUE_SETS.put(OXYGENATION_STATUS_ATTR_NAME, Arrays.asList(OXYGENATION_STATUS_VALUE_SET));
    VALUE_SETS.put(PROFILE_POSITION_ATTR_NAME, Arrays.asList(PROFILE_POSITION_VALUE_SET));
    VALUE_SETS.put(RELATIONSHIP_TO_OXYGEN_ATTR_NAME, Arrays.asList(RELATIONSHIP_TO_OXYGEN_VALUE_SET));
    VALUE_SETS.put(SEDIMENT_TYPE_ATTR_NAME, Arrays.asList(SEDIMENT_TYPE_VALUE_SET));
    VALUE_SETS.put(SEX_ATTR_NAME, Arrays.asList(HOST_SEX_VALUE_SET));
    VALUE_SETS.put(SPACE_TYPICAL_STATE_ATTR_NAME, Arrays.asList(SPACE_TYPICAL_STATE_VALUE_SET));
    VALUE_SETS.put(SUBSTRUCTURE_TYPE_ATTR_NAME, Arrays.asList(SUBSTRUCTURE_TYPE_VALUE_SET));
    VALUE_SETS.put(SURFACE_MATERIAL_ATTR_NAME, Arrays.asList(SURFACE_MATERIAL_VALUE_SET));
    VALUE_SETS.put(TIDAL_STAGE_ATTR_NAME, Arrays.asList(TIDAL_STAGE_VALUE_SET));
    VALUE_SETS.put(TILLAGE_ATTR_NAME, Arrays.asList(TILLAGE_VALUE_SET));
    VALUE_SETS.put(TROPHIC_LEVEL_ATTR_NAME, Arrays.asList(TROPHIC_LEVEL_VALUE_SET));
    VALUE_SETS.put(URINE_COLLECTION_ATTR_NAME, Arrays.asList(URINE_COLLECTION_VALUE_SET));
    VALUE_SETS.put(SUPER_POPULATION_ATTR_NAME, Arrays.asList(SUPER_POPULATION_VALUE_SET));
    VALUE_SETS.put(SUPER_POPULATION_DESCRIPTION_ATTR_NAME, Arrays.asList(SUPER_POPULATION_DESCRIPTION_VALUE_SET));
    VALUE_SETS.put(BUILDING_OCCUPANCY_TYPE_ATTR_NAME, Arrays.asList(BUILDING_OCCUPANCY_TYPE_VALUE_SET));
    VALUE_SETS.put(BUILDING_SETTING_ATTR_NAME, Arrays.asList(BUILDING_SETTING_VALUE_SET));
    VALUE_SETS.put(CARBAPENEMASE_ATTR_NAME, Arrays.asList(CARBAPENEMASE_VALUE_SET));
    VALUE_SETS.put(ENVIRONMENTAL_PACKAGE_ATTR_NAME, Arrays.asList(ENVIRONMENTAL_PACKAGE_VALUE_SET));
    VALUE_SETS.put(SURFACE_AIR_CONTAMINANT_ATTR_NAME, Arrays.asList(SURFACE_AIR_CONTAMINANT_VALUE_SET));
  }

  @Nonnull protected static List<String> ONTOLOGY_TERM_ATTR_NAMES = new ArrayList<>(Arrays.asList(DISEASE_ATTR_NAME,
      CHEMICAL_ADMINISTRATION_ATTR_NAME, ENVIRONMENT_BIOME_ATTR_NAME, ENVIRONMENT_FEATURE_ATTR_NAME, PHENOTYPE_ATTR_NAME,
      HOST_DISEASE_ATTR_NAME, HOST_TISSUE_SAMPLED_ATTR_NAME, ENVIRONMENT_MATERIAL_ATTR_NAME, PLANT_BODY_SITE_ATTR_NAME));

  @Nonnull protected static List<String> TERM_ATTR_NAMES = new ArrayList<>(Arrays.asList(BODY_HABITAT_ATTR_NAME, GEO_LOCATION_ATTR_NAME,
      HEALTH_STATE_ATTR_NAME, HOST_BODY_HABITAT_ATTR_NAME, PATHOGENICITY_ATTR_NAME, PLOIDY_ATTR_NAME, PROPAGATION_ATTR_NAME));

  @Nonnull protected static List<String> BOOLEAN_ATTR_NAMES = new ArrayList<>(Arrays.asList(HYSTERECTOMY_ATTR_NAME,
      MEDICAL_HISTORY_PERFORMED_ATTR_NAME, SMOKER_ATTR_NAME, TWIN_SIBLING_ATTR_NAME));

  @Nonnull protected static List<String> INTEGER_ATTR_NAMES = new ArrayList<>(Arrays.asList(EXTRACHROMOSOMAL_ELEMENTS_ATTR_NAME,
      HOST_TAXONOMY_ID_ATTR_NAME, MEDICATION_CODE_ATTR_NAME, NR_REPLICONS_ATTR_NAME, OCCUPANCY_AT_SAMPLING_ATTR_NAME));


  /**
   * Check if the value of the attribute is filled in, that is, it is a non-empty string.
   */
  protected boolean isFilledIn(String value) {
    return !value.trim().isEmpty();
  }

  @Nonnull
  protected AttributeValidationReport validateGeographicLocation(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = isValidGeographicLocation(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  @Nonnull
  protected AttributeValidationReport validateRelationshipToOxygen(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    List<String> valueSet = new ArrayList<>(Arrays.asList(RELATIONSHIP_TO_OXYGEN_VALUE_SET));
    boolean isValidFormat = valueSet.contains(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  /**
   * Check that the main location is a term from the list at http://www.insdc.org/documents/country-qualifier-vocabulary.
   * A colon is used to separate the country or ocean from more detailed information about the location,
   * eg "Canada: Vancouver" or "Germany: halfway down Zugspitze, Alps"
   */
  protected boolean isValidGeographicLocation(String location) {
    if(location.contains(Utils.LOCATION_SEPARATOR)) {
      String mainEntry = location.substring(0, location.indexOf(Utils.LOCATION_SEPARATOR));
      return Utils.getValidLocations().contains(mainEntry);
    } else {
      return Utils.getValidLocations().contains(location);
    }
  }

}
