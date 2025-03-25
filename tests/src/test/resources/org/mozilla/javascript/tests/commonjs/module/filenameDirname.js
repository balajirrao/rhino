function getFilename() {
	return __filename;
}

function getDirname() {
	return __dirname;
}

module.exports = {
	theFilename: __filename,
	theDirname: __dirname,
	getFilename: getFilename,
	getDirname: getDirname,
};
