function notFoundError(path) {
    error({
        en: `Expected input file ${path} does not exist.`,
        zh: `无法找到预期的输入文件 ${path}。`,
        ja: `期待される入力ファイル ${path} を見つかりませんでした。`
    })
}

if (labeler.projectConstructor) {
    let inputFolderName = params["inputFolderName"]
    let folder = root.resolve(inputFolderName)
    let inputFile = folder.resolve(`${moduleName}.txt`)
    let inputFilePath = inputFile.getAbsolutePath()
    if (!inputFile.exists()) {
        notFoundError(inputFilePath)
    }

    inputFilePaths = [inputFilePath]
} else {
    let inputFilePath = params["inputFile"]
    if (!inputFilePath) {
        error({
            en: `Input file is not specified.`,
            zh: `未指定输入文件。`,
            ja: `入力ファイルが指定されていません。`
        })
    }

    let inputFile = File.fromPath(inputFilePath)
    if(!inputFile.exists()) {
        notFoundError(inputFilePath)
    }
    inputFilePaths = [inputFilePath]
}
encoding = "UTF-8"
