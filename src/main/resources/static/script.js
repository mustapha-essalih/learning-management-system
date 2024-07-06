
let chapterCount = 0;
let resourceCount = 0;

function addChapter() {
    const chapterIndex = chapterCount++;
    const chapterDiv = document.createElement('div');
    chapterDiv.className = 'chapter';
    chapterDiv.id = 'chapter-' + chapterIndex;

    chapterDiv.innerHTML = `
        <label for="chapterTitle-${chapterIndex}">Chapter Title:</label>
        <input type="text" id="chapterTitle-${chapterIndex}" name="chapters[${chapterIndex}].chapterTitle" required><br><br>
        <label for="chapterFree-${chapterIndex}">Is Free:</label>
        <input type="checkbox" id="chapterFree-${chapterIndex}" name="chapters[${chapterIndex}].isFree"><br><br>
        <div id="resources-${chapterIndex}">
            <h4>Resources</h4>
        </div>
        <button type="button" onclick="addResource(${chapterIndex})">Add Resource</button><br><br>
        <button type="button" onclick="removeElement('chapter-${chapterIndex}')">Remove Chapter</button>
    `;

    document.getElementById('chapters').appendChild(chapterDiv);
}

function addResource(chapterId) {
    const resourceIndex = resourceCount++;
    const resourceDiv = document.createElement('div');
    resourceDiv.className = 'resource';
    resourceDiv.id = 'resource-' + resourceIndex;

    resourceDiv.innerHTML = `
        <label for="resourceTitle-${resourceIndex}">Resource Title:</label>
        <input type="text" id="resourceTitle-${resourceIndex}" name="chapters[${chapterId}].resources[${resourceIndex}].resourceTitle" required><br><br>
        <label for="resourceFile-${resourceIndex}">Resource File:</label>
        <input type="file" id="resourceFile-${resourceIndex}" name="chapters[${chapterId}].resources[${resourceIndex}].videos" required><br><br>
        <label for="resourceFree-${resourceIndex}">Is Free:</label>
        <input type="checkbox" id="resourceFree-${resourceIndex}" name="chapters[${chapterId}].resources[${resourceIndex}].isFree"><br><br>
        <label for="resourceDuration-${resourceIndex}">Duration:</label>
        <input type="number" id="resourceDuration-${resourceIndex}" name="chapters[${chapterId}].resources[${resourceIndex}].duration"><br><br>
        <button type="button" onclick="removeElement('resource-' + ${resourceIndex})">Remove Resource</button>
    `;

    document.getElementById('resources-' + chapterId).appendChild(resourceDiv);
}

function removeElement(elementId) {
    document.getElementById(elementId).remove();
}

function generateUniqueCourseName() {
    const timestamp = Date.now();
    return 'Course_' + timestamp;
}

function populateFormData() {
    document.getElementById('instructorId').value = '2';
    document.getElementById('category').value = 'lifestyle';
    document.getElementById('title').value = generateUniqueCourseName();
    document.getElementById('description').value = 'This is a test course description.';
    document.getElementById('level').value = 'BEGINNERS';
    document.getElementById('language').value = 'ENGLISH';
    document.getElementById('price').value = '49.99';
    document.getElementById('isFree').checked = false;
}

function sendFormData() {
    const formData = new FormData(document.getElementById('uploadForm'));
    const jwtToken = document.getElementById('jwtToken').value;

    fetch('http://localhost:8080/api/instructor/upload-course', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    populateFormData();
});

document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault();
    sendFormData();
});
