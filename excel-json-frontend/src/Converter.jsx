import { useState } from "react";
import axios from "axios";

export default function Converter() {
  const [jsonData, setJsonData] = useState(null);
  const [fileName, setFileName] = useState("converted");
  const [loading, setLoading] = useState(false);
  const [copySuccess, setCopySuccess] = useState("");

  const API_BASE_URL = "http://localhost:8080/api";

  const handleExcelUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);
    setJsonData(null);

    try {
      const response = await axios.post(`${API_BASE_URL}/excel-to-json`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setJsonData(response.data);
    } catch (error) {
      console.error("Error converting Excel to JSON:", error);
    }
    setLoading(false);
  };

  const handleJsonDownload = async () => {
    if (!jsonData) return;

    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/json-to-excel`, jsonData, {
        headers: { "Content-Type": "application/json" },
        responseType: "blob",
      });

      const blob = new Blob([response.data], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `${fileName}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error converting JSON to Excel:", error);
    }
    setLoading(false);
  };

  // 🔹 Function to Copy JSON to Clipboard
  const handleCopyToClipboard = () => {
    if (jsonData) {
      navigator.clipboard.writeText(JSON.stringify(jsonData, null, 2));
      setCopySuccess("Copied!");
      setTimeout(() => setCopySuccess(""), 2000);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-6">
      <h1 className="text-3xl font-bold mb-6">Excel & JSON Converter</h1>

      {/* 🔹 Excel to JSON */}
      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-lg mb-6">
        <h2 className="text-xl font-semibold mb-3">Excel to JSON</h2>
        <input type="file" accept=".xlsx, .xls" onChange={handleExcelUpload} className="border p-2 rounded w-full" />
        {loading && <p className="text-blue-500">Processing...</p>}
        {jsonData && (
          <div className="relative">
            <pre className="bg-gray-200 p-3 mt-3 rounded max-h-64 overflow-auto text-sm">
              {JSON.stringify(jsonData, null, 2)}
            </pre>
            <button
              onClick={handleCopyToClipboard}
              className="absolute top-2 right-2 bg-blue-500 text-white px-2 py-1 rounded text-xs"
            >
              {copySuccess || "Copy"}
            </button>
          </div>
        )}
      </div>

      {/* 🔹 JSON to Excel */}
      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-lg">
        <h2 className="text-xl font-semibold mb-3">JSON to Excel</h2>
        <textarea
          className="border p-2 rounded w-full h-32"
          placeholder="Paste JSON here..."
          onChange={(e) => {
            try {
              setJsonData(JSON.parse(e.target.value));
            } catch (error) {
              console.error("Invalid JSON input");
            }
          }}
        ></textarea>
        <input
          type="text"
          placeholder="Enter file name"
          className="border p-2 rounded w-full mt-3"
          value={fileName}
          onChange={(e) => setFileName(e.target.value)}
        />
        <button onClick={handleJsonDownload} className="bg-blue-500 text-white px-4 py-2 rounded mt-3 w-full">
          {loading ? "Processing..." : "Download Excel"}
        </button>
      </div>
    </div>
  );
}
