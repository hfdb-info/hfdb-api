# searchName

returns all skus associated with specified nam with optional filters
`http://localhost:8080/searchName/{name}/{min}/{max}`

name: a single word search term
min: optional minimum price floor, circumvented by inputting `-1`
max: optional maximum price ceiling, circumvented by inputting `-1`

Example with filter:`http://localhost:8080/searchName/paint/10000/100000`

Example without filter: `http://localhost:8080/searchName/paint/-1/-1`

Example Output: returns an array of key value pairs where `sku` is the key which returns an integer with a specific sku

```json
[
    {
        "sku": 56955
    },
    {
        "sku": 94605
    },
   ...
]
```

# grabProductDetails

Returns the sku, as well as the name, imgURL, canonicalURL, most up-to-date price, and timestamp (ts) that this price was scraped

`http://localhost:8080/grabProductDetails/{sku}`

Example Call: `http://localhost:8080/grabProductDetails/56955`

Example Output: returns an object as below, otherwise returns 404 if the sku is not found

```json
{
  "sku": 56955,
  "name": "2 in. Flat Paint Brush, GOOD Quality",
  "imgURL": "https://www.harborfreight.com/media/catalog/product/cache/05fa449c5750256d44c80bdb706bae27/5/6/56955_W3.jpg",
  "canonicalURL": "2-in-flat-paint-brush-good-quality-56955.html",
  "price": 199,
  "ts": "2022-09-21 03:17:39-05"
}
```
