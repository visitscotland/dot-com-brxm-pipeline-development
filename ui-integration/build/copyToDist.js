const path = require('path');
const { promises: fs } = require('fs');

async function copyDir(src, dest) {
  let entries = await fs.readdir(src, { withFileTypes: true });
  await fs.mkdir(dest, { recursive: true });
  for (let entry of entries) {
    let srcPath = path.join(src, entry.name);
    let destPath = path.join(dest, entry.name);
    entry.isDirectory() ?
      await copyDir(srcPath, destPath) :
      await fs.copyFile(srcPath, destPath);
  }
}

/* 
 * Perform any post-copy actions here 
 *
 * NOTE: Throwing here will stop processing of the promise chain so further copy 
 *       operations will not be executed
 */
function postCopy(src, dst) {
  // For example, check that required files and directories are now present in 'dist'
}

/* 
 * Handle any copy errors here
 *
 * 1) Returning a regular value from a reject handler, causes the next .then() resolve 
 *    handler to be called (e.g. normal processing continues),
 *
 * 2) Throwing in a reject handler causes normal resolve processing to stop and all 
 *    resolve handlers are skipped until you get to a reject handler or the end of 
 *    the chain. This is effective way to stop the chain if an unexpected error is found 
 *    in a resolve handler (which I think is your question).
 *
 * 3) Not having a reject handler present causes normal resolve processing to stop and 
 *    all resolve handlers are skipped until you get to a reject handler or the end of 
 *    the chain.
*/
function handleError(reason) {
  //if (reason instanceof criticalError) {
  //  throw reason;
  //}
  //console.info(reason);
}

function copyToDist() {
  
  const toCopy = [
    { src: '../frontend/ssr', dst: 'dist/ssr' },
    { src: '../frontend/dist/ssr/client', dst: 'dist/ssr/client' },
    { src: '../frontend/dist/ssr/server', dst: 'dist/ssr/server' },
    { src: '../frontend/dist/library', dst: 'dist/library' },
    { src: '../repository-data/webfiles/src/main/resources/site', dst: 'dist/repository-data/webfiles/src/main/resources/site' },
  ];

  toCopy.forEach( e => {
    copyDir(path.resolve(e.src), path.resolve(e.dst))
      .then(postCopy(e.src, e.dst))
      .catch(handleError);
  });
}

// Top-level script execution
copyToDist()