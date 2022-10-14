package info.hfdb.hfdbapi.Controller.dao;

import java.util.List;
import java.util.Optional;

import info.hfdb.hfdbapi.Controller.Product;
import info.hfdb.hfdbapi.Controller.ProductSKU;

public interface HelperDao {

    /**
     * This provides access to the data necessary to run the SQL select statments
     *
     * @param name the name to be searched.
     * @param min  the minimum price to search by.
     * @param max  the maximum price to search by.
     * @return a list of ProductSKUs that match the given parameters.
     */
    List<ProductSKU> nameSearch(String name, int min, int max);

    /**
     * This provides access to the data necessary to run the SQL select statments
     *
     * @param sku the sku to enter as the serarch term.
     * @return a list of product details that match the given sku
     */
    Optional<Product> grabProductDetails(int sku);

}
