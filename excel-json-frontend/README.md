# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript and enable type-aware lint rules. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

## Excel & JSON Converter

This project includes an Excel & JSON converter tool. The tool allows you to:

- Convert Excel files to JSON format.
- Convert JSON data to Excel files.
- Copy JSON data to the clipboard.

### Usage

1. **Excel to JSON**:
   - Upload an Excel file (.xlsx or .xls).
   - The JSON data will be displayed and can be copied to the clipboard.

2. **JSON to Excel**:
   - Paste JSON data into the provided textarea.
   - Enter a file name for the Excel file.
   - Download the converted Excel file.

### APIs

The project uses the following APIs:

- **Excel to JSON**: `POST /api/excel-to-json`
  - Endpoint to convert an uploaded Excel file to JSON format.
  - Request: Multipart form data with the Excel file.
  - Response: JSON data.

- **JSON to Excel**: `POST /api/json-to-excel`
  - Endpoint to convert JSON data to an Excel file.
  - Request: JSON data.
  - Response: Excel file (blob).

### Development

To start the development server, run:

```sh
npm run dev