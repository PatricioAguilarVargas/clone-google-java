let aux = document.location.href.split("?query=");
let query = aux[1];
document.getElementById("txtSearch").value = query

fetch("http://localhost:8082/api/search?query=" + query)
.then(response => {
	return response.json();
})
.then(json =>{
    let html = "";
	for(let resultSearch of json){
		html +=  getHtmlResultSearch(resultSearch);
	}
	document.getElementById('links').innerHTML = html;
});

function getHtmlResultSearch(resultSearch){
	return `<div class="link">
                <a style="text-decoration: none" target="_blank" href="${resultSearch.url}"><h3>${resultSearch.title}</h3></a>
                <span>${resultSearch.description} ...</span>
            </div>`;
}