import fs from "fs";
import path from "path";

export const getIndexHtml = () => {
  let indexHtml = fs.readFileSync(path.join(__dirname, '..', 'build', 'index.html'), 'utf-8');
  (indexHtml.match(/%.+?%/g) || []).forEach((m) => {
    indexHtml = indexHtml.split(m).join(process.env[m.replace(/%/g, '')]);
  })
  return indexHtml;
}
