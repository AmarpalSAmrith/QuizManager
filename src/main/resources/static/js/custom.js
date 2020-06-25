// function myFunction(identifier, element) {
function myFunction(element) {
  var selectElement = document.getElementById(element);
  selectElement.parentNode.removeChild(element);
}