package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.AssetSecurityPropertyPair;
import uk.ac.city.toreador.rest.api.entities.AssetsSecuritypropertiesId;

public interface AssetSecurityPropertyPairsRepository extends CrudRepository<AssetSecurityPropertyPair, AssetsSecuritypropertiesId> {

}
